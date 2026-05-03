# Auth0 setup checklist for a React + FastAPI app

## Auth0 dashboard

- Create or select an Auth0 **Single Page Application**.
- Record:
  - Domain
  - Client ID
- Create or select an Auth0 **API**.
- Record:
  - API Identifier / Audience
- Ensure the API signing algorithm is **RS256**.
- Add the frontend URL to:
  - Allowed Callback URLs
  - Allowed Logout URLs
  - Allowed Web Origins
- If using RBAC/permissions:
  - Define permissions such as `read:profile`, `read:messages`, `write:messages`
  - Enable RBAC if your tenant/app setup requires it

## Frontend env

Typical Vite variables:

```env
VITE_AUTH0_DOMAIN=
VITE_AUTH0_CLIENT_ID=
VITE_AUTH0_AUDIENCE=
VITE_API_BASE_URL=http://localhost:8000
```

Typical CRA variables:

```env
REACT_APP_AUTH0_DOMAIN=
REACT_APP_AUTH0_CLIENT_ID=
REACT_APP_AUTH0_AUDIENCE=
REACT_APP_API_BASE_URL=http://localhost:8000
```

## Backend env

```env
AUTH0_DOMAIN=
AUTH0_AUDIENCE=
BACKEND_CORS_ORIGINS=http://localhost:3000,http://localhost:5173
```

## Minimum acceptance criteria

- Login works through Auth0 Universal Login.
- Logout returns to the app correctly.
- Frontend can obtain an Auth0 access token for the backend audience.
- Protected FastAPI routes reject missing/invalid tokens with 401.
- Scope-protected routes reject insufficient permissions with 403.
- A protected frontend screen can successfully call a protected backend endpoint.
