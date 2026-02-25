# Integrations

## Auth0 (OIDC Identity Provider)

### Overview
Auth0 is used as the sole identity provider via OpenID Connect (OIDC).

### Library
Authlib (Python) for OIDC client integration.

### Flow
1. User clicks "Login" -> redirected to Auth0 Universal Login
2. Auth0 authenticates user -> redirects to `/auth/callback/` with authorization code
3. Backend exchanges code for tokens (access token, ID token)
4. Backend extracts user info from ID token (sub, email, name)
5. Backend upserts user in `users` table (lookup by auth0_sub)
6. Backend stores user details in Django session (user_id, email, name, role, auth0_sub)
7. User is redirected to dashboard or home page

### Logout Flow
1. User clicks "Logout"
2. Django session is cleared
3. User is redirected to Auth0 logout endpoint
4. Auth0 redirects back to home page

### Configuration (Environment Variables)
- AUTH0_DOMAIN
- AUTH0_CLIENT_ID
- AUTH0_CLIENT_SECRET
- AUTH0_CALLBACK_URL

### User Upsert Logic
On callback:
- Query users table by auth0_sub
- If not found: create new user with role="author", is_active=True
- If found: update name, email, last_login_at
- Store user details in session

### Security
- Tokens validated server-side
- State parameter for CSRF protection in OIDC flow
- Session cookie: httpOnly, secure, SameSite=Lax

---

## Future Extensions (Not Currently Required)

- Email notifications on approval
- Webhook when post published
- Analytics tracking

Any new integration requires Supervisor promotion approval.
