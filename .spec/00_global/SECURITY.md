# Security Standards

## Authentication

- All endpoints require authentication unless explicitly public.
- Tokens/cookies validated server-side.
- Expiration enforced.

## Authorization

- Role or permission-based access control.
- Resource-level authorization where applicable.
- Never trust client claims.

## Input Validation

- Validate at boundary.
- Sanitize user-generated content.
- Prevent injection attacks.

## Secrets Management

- No secrets in repository.
- Environment variables for configuration.
- Rotate credentials periodically.

## Rate Limiting

- Protect public endpoints.
- Prevent brute-force attacks.

## Logging

- Log security-relevant events.
- Do not log passwords or sensitive tokens.

## Data Protection

- Encrypt in transit (HTTPS mandatory).
- Encrypt sensitive data at rest when required.
- Follow least-privilege DB access.

## Dependency Hygiene

- Keep dependencies updated.
- Monitor known vulnerabilities.
