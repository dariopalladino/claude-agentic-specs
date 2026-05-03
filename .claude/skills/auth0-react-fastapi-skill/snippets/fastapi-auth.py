from __future__ import annotations

import os
from functools import lru_cache
from typing import Any, Dict, List

import jwt
from fastapi import Depends, HTTPException, Security, status
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from jwt import PyJWKClient

bearer_scheme = HTTPBearer(auto_error=False)


class AuthError(HTTPException):
    def __init__(self, detail: str, status_code: int = status.HTTP_401_UNAUTHORIZED):
        super().__init__(status_code=status_code, detail=detail)


@lru_cache(maxsize=1)
def get_auth0_settings() -> Dict[str, str]:
    domain = os.getenv('AUTH0_DOMAIN', '').strip()
    audience = os.getenv('AUTH0_AUDIENCE', '').strip()
    if not domain or not audience:
        raise RuntimeError('AUTH0_DOMAIN and AUTH0_AUDIENCE must be set')

    issuer = f"https://{domain}/"
    jwks_url = f"{issuer}.well-known/jwks.json"
    return {
        'domain': domain,
        'audience': audience,
        'issuer': issuer,
        'jwks_url': jwks_url,
    }


@lru_cache(maxsize=1)
def get_jwks_client() -> PyJWKClient:
    settings = get_auth0_settings()
    return PyJWKClient(settings['jwks_url'])


def decode_token(token: str) -> Dict[str, Any]:
    settings = get_auth0_settings()
    signing_key = get_jwks_client().get_signing_key_from_jwt(token)

    return jwt.decode(
        token,
        signing_key.key,
        algorithms=['RS256'],
        audience=settings['audience'],
        issuer=settings['issuer'],
    )


def get_current_claims(
    credentials: HTTPAuthorizationCredentials | None = Security(bearer_scheme),
) -> Dict[str, Any]:
    if credentials is None or credentials.scheme.lower() != 'bearer':
        raise AuthError('Missing bearer token')

    token = credentials.credentials
    try:
        return decode_token(token)
    except jwt.ExpiredSignatureError as exc:
        raise AuthError('Token expired') from exc
    except jwt.InvalidTokenError as exc:
        raise AuthError('Invalid token') from exc


def _extract_scopes(claims: Dict[str, Any]) -> List[str]:
    permissions = claims.get('permissions')
    if isinstance(permissions, list):
        return [str(p) for p in permissions]

    scope = claims.get('scope', '')
    if isinstance(scope, str):
        return [item for item in scope.split() if item]

    return []


def require_scopes(required_scopes: List[str]):
    def dependency(claims: Dict[str, Any] = Depends(get_current_claims)) -> Dict[str, Any]:
        token_scopes = set(_extract_scopes(claims))
        missing = [scope for scope in required_scopes if scope not in token_scopes]
        if missing:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail=f"Missing required scopes: {', '.join(missing)}",
            )
        return claims

    return dependency
