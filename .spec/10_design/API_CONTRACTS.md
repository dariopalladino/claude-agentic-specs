# API_CONTRACTS.md

# 1. Guiding Principles

All API endpoints must be:

* Deterministic
* Typed (via Pydantic schemas for validation)
* Domain-organized
* Testable
* Observable
* Secure by default

Route handlers must **never contain business logic**.
They should only orchestrate dependencies.

Layer responsibilities:

```
FastAPI Router -> Service -> Repository -> SQLAlchemy Async Session -> Database
```

---

# 2. Project Structure Contract

All backend endpoints follow FastAPI project structure in `backend/`:

```
backend/
  app/
    main.py                 # FastAPI app creation and router mounting
    api/
      dependencies/         # Auth, role, pagination, request context dependencies
      v1/
        auth.py             # Auth-related endpoints
        users.py            # User identity endpoints
        <domain>.py         # Domain routers
    core/
      exceptions.py         # Domain and application exceptions
    policies/               # RBAC/ownership policy logic
    services/               # Business logic
    repositories/           # Persistence logic
    schemas/                # Pydantic request/response models
    db/
      session.py            # Async engine/session factory
      models/               # SQLAlchemy models
```

---

# 3. URL Naming Rules

## URL Patterns

Use plural nouns, clean paths.

Use explicit API versioning.

```
GET    /api/v1/health
GET    /api/v1/<resources>
GET    /api/v1/<resources>/{id}
POST   /api/v1/<resources>
PATCH  /api/v1/<resources>/{id}
DELETE /api/v1/<resources>/{id}
```

No server-rendered page routes are part of this contract.

---

# 4. Router Contract

Each domain must have its own FastAPI router module.

Router rules:
* No DB access in routers directly (use services)
* No business logic in routers
* Request parsing + dependency orchestration + response serialization only
* Use typed request and response models for all non-trivial endpoints
* Raise HTTP errors via standardized exception mapping

---

# 5. Service Layer Contract

The service layer contains business logic.

Rules:
* No FastAPI framework coupling in core business logic where avoidable
* Pure Python logic
* Fully unit testable
* Receives Pydantic schemas or plain data, returns domain objects

---

# 6. Data Access Contract

SQLAlchemy async sessions are used for all database access.

Rules:
* Only persistence logic
* No business logic
* SQLAlchemy 2 async API only
* Sessions obtained from async session factory in backend DB session module
* PostgreSQL driver must be `asyncpg`

---

# 7. Schema Contract (Pydantic)

Each domain must define:

```
<Domain>Create
<Domain>Update
<Domain>Read
UserRead
ErrorResponse
```

Never expose internal DB fields unintentionally.

Input and output models MUST be separated when write and read concerns differ.

---

# 8. Error Handling Contract

Use domain exceptions in `backend/app/core/exceptions.py`.

FastAPI exception handlers must map domain exceptions to stable JSON error responses.

Error responses must include:
* machine-readable error code
* human-readable message
* optional field-level details
* trace or correlation identifier when available

---

# 9. Authorization Contract

All protected endpoints must check:
* User is authenticated via validated OIDC JWT
* JWT validation includes `iss`, `aud`, `exp`, signature, and JWKS-based key resolution
* User is resolved to internal user record
* User role model supports baseline roles `user` and `admin`
* User has required role (RBAC)
* User owns the resource where applicable

Authorization is enforced server-side via dependencies/middleware/policy services.

Frontend clients (React and Kotlin) must be treated as untrusted.

---

# 10. Pagination Contract

List views must support pagination.

Default: 20 items per page.
Use explicit query params (for example, `limit`, `offset`, and optional cursor where needed).

Pagination metadata must be returned in response payload.

---

# 11. Testing Contract

Every endpoint/domain requires:
* API integration test
* Service test (unit)
* Repository test (integration or transactional)

Minimum coverage target: 80%

Required tooling baseline:
* `pytest`
* `pytest-asyncio`
* `mypy`

---

# 12. Security Contract

All non-public endpoints must verify validated bearer tokens.

For API-only architecture with bearer auth, CSRF defenses are not a primary control for token-based endpoints; CORS, token validation, and origin hardening are mandatory controls.

Never trust client-provided user IDs.

The authenticated user identity must come from verified token claims plus server-side user lookup.

---

# 13. Client Compatibility Contract

The backend contract MUST be client-agnostic and support:
* Web frontend in React (TypeScript)
* Native mobile app in Kotlin

Rules:
* No server-rendered HTML dependencies in API behavior
* JSON request/response only
* Stable versioned endpoints and backward-compatible evolution within a version
* OpenAPI schema must be kept accurate for client code generation and validation

---

# End of Contract
