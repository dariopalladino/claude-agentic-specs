---
name: auth0-react-fastapi
description: Implements Auth0 authentication for React, FastAPI, and native Android applications (XML and Jetpack Compose) with PKCE, Universal Login, JWT validation, RBAC, secure storage, and refresh token rotation.
when_to_use: Trigger for requests like 'add auth', 'add login/signup', 'protect this React + FastAPI app', 'integrate Auth0', 'implement JWT auth', 'add role-based access control', 'secure API routes', or 'wire social login with Auth0'. Best for SPAs using React 18+ and APIs using FastAPI.
allowed-tools: Read Edit MultiEdit Write Grep Glob Bash(git *) Bash(ls *) Bash(find *) Bash(cat *) Bash(python *)
---

# Purpose

Use this skill to implement **production-grade Auth0 authentication** for a **React SPA + FastAPI API** codebase.

This skill should guide Claude Code to:

1. Detect the frontend and backend structure.
2. Add Auth0 to the React app using the official React SDK.
3. Configure the frontend to request API access tokens for the FastAPI backend.
4. Add JWT validation in FastAPI against Auth0's JWKS using issuer and audience checks.
5. Protect routes and endpoints.
6. Optionally add RBAC/scope checks.
7. Update environment configuration, docs, and tests.
8. Leave the repo in a runnable state with minimal surprises.

Auth0's React SDK is intended for SPAs and uses **Universal Login** and the **Authorization Code Flow with PKCE**. The React app should be wrapped in `Auth0Provider` and use `loginWithRedirect`, `logout`, and `getAccessTokenSilently` where appropriate. Auth0's React quickstart documents these patterns. citeturn421362view1turn421362view2

Claude Code skills are defined with a `SKILL.md` file containing YAML frontmatter plus markdown instructions, and can live under `~/.claude/skills/<skill-name>/SKILL.md` or `.claude/skills/<skill-name>/SKILL.md`. The `description` field is used by Claude Code to decide when to apply the skill. citeturn421362view0turn997753view1turn997753view3

---

# Default architecture

Unless the repository already dictates otherwise, prefer this architecture:

- **Frontend**: React SPA uses `@auth0/auth0-react`.
- **Login UX**: Auth0 Universal Login.
- **Token model**:
  - Frontend authenticates the user.
  - Frontend requests an **access token** for the backend API using the Auth0 **audience**.
  - Frontend sends `Authorization: Bearer <token>` to FastAPI.
  - FastAPI validates JWT signature and claims using Auth0 issuer, audience, and JWKS.
- **Authorization**:
  - Authentication required for protected endpoints.
  - Scope or permission checks layered on top when requested.

Do **not** implement password storage, session login, or homemade JWT signing when the user requested Auth0.

---

# Workflow Claude should follow

## 1) Inspect the repo before changing anything

First map the codebase.

Look for:

- Frontend package manager and build system: Vite, CRA, Next-in-SPA mode, etc.
- Router: React Router or custom routing.
- Existing auth code, protected route wrappers, API client utilities, interceptors.
- Backend entrypoint: `main.py`, `app/main.py`, `src/main.py`, etc.
- Existing dependencies related to JWT, auth, or CORS.
- Existing env files and config conventions.
- Existing user/role model that might consume Auth0 claims.

Then summarize the likely integration points before editing files.

## 2) Choose the least-invasive integration

Prefer targeted changes over broad rewrites.

- Reuse existing providers and app wrappers.
- Reuse existing API client layers for bearer token injection.
- Reuse existing route conventions.
- Keep auth concerns centralized.

If multiple patterns are possible, prefer:

- one auth provider module on the frontend,
- one backend auth module/dependency,
- one env contract,
- one short documentation section.

## 3) Add frontend Auth0 integration

Use `@auth0/auth0-react`.

Minimum frontend tasks:

- Add dependency.
- Wrap the app with `Auth0Provider`.
- Use:
  - `domain`
  - `clientId`
  - `authorizationParams.redirect_uri`
- When the frontend must call the backend API, configure audience and optionally scopes in `authorizationParams` or token fetch calls.
- Add login and logout actions.
- Add a current-user/profile hook or component if appropriate.
- Add a protected route wrapper if the app uses routing.
- Add an authenticated API client helper that gets a token via `getAccessTokenSilently()`.

Implementation guidance:

- Prefer a dedicated `AuthProvider`/`auth.ts` wrapper around the SDK when the app already uses app-level providers.
- Keep SDK specifics out of most feature components.
- Avoid reading tokens from local storage manually unless the repo already requires a documented caching strategy.
- If API requests are centralized, inject the bearer token there instead of sprinkling token logic throughout the app.

## 4) Add backend JWT validation for Auth0 access tokens

FastAPI should validate incoming bearer tokens against Auth0 using:

- **issuer**: `https://<AUTH0_DOMAIN>/`
- **audience**: Auth0 API Identifier
- **JWKS**: fetched from Auth0's tenant JWKS endpoint
- **algorithm**: typically RS256

Add a reusable backend auth module that provides:

- token extraction from the `Authorization` header,
- JWT verification,
- claim normalization,
- a `require_auth` dependency,
- an optional `require_scopes([...])` dependency.

Implementation guidance:

- Use a dependency instead of repeating verification inside each endpoint.
- Return `401` for invalid/missing token and `403` for insufficient permissions.
- Preserve decoded claims on `request.state` or return them from the dependency when that fits the codebase.
- Keep the verifier isolated in one module such as `auth.py`, `security.py`, or `core/auth.py`.
- Ensure CORS permits the frontend origin and the `Authorization` header.

## 5) Wire protected API calls end to end

After both sides are in place:

- Add or update at least one protected backend endpoint.
- Make the frontend call it with a valid Auth0 access token.
- Handle loading, unauthenticated, and unauthorized UI states cleanly.

## 6) Add RBAC or scopes when requested

If the user asks for roles, permissions, or admin-only routes:

- Prefer checking scopes/permissions included in the access token.
- Add a reusable backend dependency such as `require_permissions(["read:messages"])` or `require_scopes([...])`.
- If the frontend needs feature gating, use claims defensively for UI only; the backend must remain the source of truth.

## 7) Update configuration and docs

You must update the env and setup docs.

Frontend env usually needs:

- `VITE_AUTH0_DOMAIN` or `REACT_APP_AUTH0_DOMAIN`
- `VITE_AUTH0_CLIENT_ID` or `REACT_APP_AUTH0_CLIENT_ID`
- `VITE_AUTH0_AUDIENCE` or `REACT_APP_AUTH0_AUDIENCE`
- frontend origin / redirect URI if not inferred

Backend env usually needs:

- `AUTH0_DOMAIN`
- `AUTH0_AUDIENCE`
- allowed CORS origin(s)

Document Auth0 dashboard settings the developer must configure:

- Application type should match SPA usage.
- Set the Allowed Callback URL(s).
- Set the Allowed Logout URL(s).
- Set Allowed Web Origin(s).
- Create or configure an API and use its Identifier as the audience.

Auth0's React quickstart specifically calls out the need for **Domain**, **Client ID**, callback URL, logout URL, and wrapping the app in `Auth0Provider`. It also recommends using the Auth0 React SDK for React 18 SPAs. citeturn421362view1turn421362view2

## 8) Validate the result

Before finishing, verify as much as the repo allows:

- frontend builds,
- backend starts,
- lint/type checks pass if present,
- imports resolve,
- no stale references remain,
- docs match actual env variable names,
- protected route flow is coherent.

If full runtime validation is impossible because tenant credentials are unavailable, say so explicitly and provide the exact manual verification steps.

---

# Coding standards for this skill

## Frontend

- Keep auth code in a small number of files.
- Prefer typed helpers in TypeScript repos.
- Avoid unnecessary global state libraries if the SDK already provides context.
- Show clear loading states while Auth0 initializes.
- Gracefully handle SDK errors.
- Do not expose secrets; SPA uses public client-side identifiers only.

## Backend

- Keep auth checks dependency-based and composable.
- Isolate third-party JWT/JWKS logic from endpoint code.
- Use clear exception messages but do not leak sensitive internals.
- Avoid mixing Auth0 JWT verification with app-issued JWTs unless the repo already intentionally supports both.

## Documentation

Always leave:

1. a concise setup section,
2. required Auth0 dashboard settings,
3. env variables,
4. local run steps,
5. at least one example protected endpoint and request flow.

---

# Implementation checklist

When invoked, work through this checklist and explicitly track progress:

- [ ] Detect frontend entrypoint and provider tree
- [ ] Detect backend entrypoint and middleware/dependencies
- [ ] Identify env/config pattern already used in repo
- [ ] Add Auth0 React SDK integration
- [ ] Add login/logout and authenticated user state
- [ ] Add token-aware API client helper
- [ ] Add FastAPI JWT verification module
- [ ] Add protected dependency for authenticated routes
- [ ] Add optional scope/permission dependency if needed
- [ ] Update CORS configuration
- [ ] Add or update protected example endpoint + frontend call
- [ ] Update `.env.example` / config docs
- [ ] Update README/setup instructions
- [ ] Run project validation commands when available

---

# File patterns to prefer

These are suggestions, not rigid requirements.

## React

Common good targets:

- `src/main.tsx`
- `src/main.jsx`
- `src/index.tsx`
- `src/providers/AuthProvider.tsx`
- `src/lib/auth.ts`
- `src/lib/api.ts`
- `src/routes/ProtectedRoute.tsx`
- `src/components/auth/LoginButton.tsx`
- `src/components/auth/LogoutButton.tsx`

## FastAPI

Common good targets:

- `app/main.py`
- `main.py`
- `app/core/auth.py`
- `app/core/security.py`
- `app/dependencies/auth.py`
- `app/api/deps.py`
- `app/api/routes/*.py`

---

# Common implementation pattern

## Frontend wrapper

Typical responsibilities:

- initialize `Auth0Provider`,
- expose an auth-aware API caller,
- trigger redirect login,
- perform logout with return URL,
- optionally expose decoded user profile.

## Backend verifier

Typical responsibilities:

- parse bearer token,
- fetch JWKS and select key by `kid`,
- verify signature and standard claims,
- enforce issuer and audience,
- expose claims to route handlers,
- optionally enforce scopes/permissions.

---

# Manual verification flow to document

Always leave the developer with a crisp verification flow:

1. Create/configure Auth0 SPA application.
2. Create/configure Auth0 API with identifier.
3. Fill frontend and backend env vars.
4. Start backend.
5. Start frontend.
6. Click login.
7. Complete Auth0 Universal Login.
8. Open a protected page.
9. Trigger a frontend request to a protected FastAPI endpoint.
10. Confirm 200 with token and 401/403 without proper auth.

---

# Pitfalls to avoid

- Forgetting to request the backend audience, causing opaque or wrong tokens for the API.
- Using ID tokens to call the backend instead of access tokens.
- Forgetting callback/logout/web origin settings in Auth0.
- Hardcoding tenant values instead of using env vars.
- Protecting frontend routes but leaving backend endpoints open.
- Trusting frontend-only role checks for authorization.
- Returning 500s for expected auth failures.
- Breaking existing fetch/axios wrappers instead of extending them.

---

# If the repo already has partial auth

When partial auth already exists:

- preserve working pieces where reasonable,
- remove conflicting fake auth only if it clearly blocks Auth0 integration,
- migrate progressively rather than rewrite unrelated features,
- document what changed and any follow-up cleanup items.

---

# Expected output from Claude when using this skill

Claude should finish with:

1. a short summary of what was added,
2. the exact files changed,
3. env vars required,
4. Auth0 dashboard settings required,
5. commands run for validation,
6. anything not fully verified because credentials were unavailable.

---

# Supporting files in this skill

- `templates/auth0-setup-checklist.md` — copyable setup checklist for the target repo.
- `snippets/react-auth-wrapper.tsx` — example React integration pattern.
- `snippets/fastapi-auth.py` — example FastAPI dependency-based JWT validation structure.
- `examples/manual-test-flow.md` — example end-to-end verification flow.

Load them when useful, but adapt them to the repository instead of pasting blindly.

--- 

# Native Android (Auth0 Integration)
When the user requests authentication for a native Android app, implement Auth0 using the official Android SDK with Authorization Code Flow + PKCE via Universal Login.

## Required SDK
Use: com.auth0.android:auth0

## Implementation Steps (Android)
1. Configure Auth0 Application
- Type: Native Application
- Set:
  Callback URL: your.app.id://YOUR_DOMAIN/android/your.app.id/callback
  Logout URL: your.app.id://YOUR_DOMAIN/android/your.app.id/logout

2. Add Dependencies
In build.gradle: implementation 'com.auth0.android:auth0:2.+'

3. Configure Auth0 in App
```
val account = Auth0(
    "YOUR_CLIENT_ID",
    "YOUR_DOMAIN"
)
```

4. Implement Login (Universal Login)
```
WebAuthProvider.login(account)
    .withScheme("your.app.id")
    .withAudience("YOUR_API_AUDIENCE") // if using API
    .start(this, object : Callback<Credentials, AuthenticationException> {
        override fun onSuccess(credentials: Credentials) {
            val accessToken = credentials.accessToken
            val idToken = credentials.idToken
        }

        override fun onFailure(error: AuthenticationException) {
            // handle error
        }
    })
```

5. Handle Callback in Manifest
```
<activity
    android:name="com.auth0.android.provider.RedirectActivity"
    android:exported="true">
    <intent-filter>
        <data
            android:scheme="your.app.id"
            android:host="YOUR_DOMAIN"
            android:pathPrefix="/android/your.app.id/callback" />
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
    </intent-filter>
</activity>
```

6. Logout
```
WebAuthProvider.logout(account)
    .withScheme("your.app.id")
    .start(this, object : Callback<Void?, AuthenticationException> {
        override fun onSuccess(payload: Void?) {}

        override fun onFailure(error: AuthenticationException) {}
    })
```

7. Secure API Calls
- Send access token in headers: Authorization: Bearer <access_token>
- Backend (FastAPI) must validate:
  - iss
  - aud
  - JWT signature via JWKS

## Best Practices (Android)
- Always use PKCE (handled automatically)
- Never embed secrets in the app
- Store tokens securely using:
  - EncryptedSharedPreferences or SecureStorage
- Use refresh tokens with rotation if long sessions are required
- Prefer Universal Login over embedded login

## Integration with FastAPI Backend
Ensure:
- Audience matches backend API identifier
- Tokens from Android are accepted the same as React SPA tokens
- RBAC scopes are enforced consistently

## Refresh Token Rotation (Android)
When long-lived sessions are required, implement Refresh Token Rotation with Auth0.
### Enable in Auth0 Dashboard
- Go to Application → Advanced Settings → OAuth
- Enable:
✅ Refresh Token Rotation
✅ Absolute Expiration
✅ Reuse Interval (recommended: small window like 30s)

### Token Strategy (Android)
Claude Code MUST implement:
- Store:
  - access_token (short-lived)
  - refresh_token (rotating)
- Never store tokens in plain SharedPreferences
- Use secure storage

### Secure Token Storage
Use Android secure storage:
```
val securePrefs = EncryptedSharedPreferences.create(
    "auth_prefs",
    MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build(),
    context,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

### Refresh Access Token
Use Auth0 Authentication API:
```
AuthenticationAPIClient(account)
    .renewAuth(refreshToken)
    .start(object : Callback<Credentials, AuthenticationException> {
        override fun onSuccess(credentials: Credentials) {
            // IMPORTANT: replace BOTH tokens
            saveAccessToken(credentials.accessToken)
            saveRefreshToken(credentials.refreshToken)
        }

        override fun onFailure(error: AuthenticationException) {
            // If refresh fails → force re-login
        }
    })
```

### Critical Rotation Rule
Claude Code MUST enforce:
- Every refresh returns a new refresh token
- Always overwrite the old refresh token
- If refresh fails → logout user immediately

### Logout Handling
- Clear:
    - access token
    - refresh token
    - user profile
- Also call Auth0 logout endpoint

## Snippets to reference
Use snippets as reference implementations, but adapt to the target repo structure and coding style:
- snippets/android-secure-storage.kt
- snippets/android-refresh-token.kt
- snippets/android-compose-auth.kt
- snippets/android-auth0-login.kt