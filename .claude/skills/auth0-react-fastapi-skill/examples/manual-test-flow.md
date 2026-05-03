# Manual verification flow

## Happy path

1. Start the FastAPI backend.
2. Start the React frontend.
3. Open the app in the browser.
4. Click **Log in**.
5. Complete Auth0 Universal Login.
6. Return to the app.
7. Visit a protected page or click a button that calls a protected API endpoint.
8. Confirm the request succeeds with HTTP 200.
9. Confirm the UI can display authenticated user information or protected data.

## Unauthenticated behavior

1. Open the protected page in a fresh/private session.
2. Confirm the app redirects to login or shows an unauthenticated state.
3. Call the protected API manually without a bearer token.
4. Confirm FastAPI returns HTTP 401.

## Unauthorized behavior

1. Log in with a user lacking the required scope/permission.
2. Call a scope-protected endpoint.
3. Confirm FastAPI returns HTTP 403.

## Common debugging checks

- The token being sent is an **access token**, not an ID token.
- The token `aud` matches `AUTH0_AUDIENCE`.
- The backend issuer is exactly `https://<AUTH0_DOMAIN>/`.
- Frontend origin appears in Allowed Web Origins.
- Redirect URI appears in Allowed Callback URLs.
- Logout return URI appears in Allowed Logout URLs.
