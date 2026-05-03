# Foundation

This document defines **mandatory conventions** for building any project in this repository.

The goal is to ensure:

* Consistent architecture across backend, native, and web
* Predictable quality gates for delivery
* Security-first authentication patterns
* Maintainable and testable codebases

All implementation MUST comply with this specification.

---

## 1. Scope

All new projects and major refactors MUST follow this stack split:

* Backend: `backend/`
* Native Android: `native/`
* Web frontend: `web/`

Technology substitutions are not allowed unless a separate approved design decision document explicitly overrides this file.

---

## 2. Mandatory Technology Baseline

### 2.1 Backend (`backend/`)

The backend MUST use:

* Python 3.12+
* FastAPI + Starlette + Uvicorn
* SQLAlchemy 2.x (async usage)
* Alembic migrations
* PostgreSQL with `asyncpg`
* Auth0 JWT verification using `python-jose` + JWKS retrieval/validation
* OpenAI SDK (async client usage)
* Pytest + `pytest-asyncio` + `mypy`

### 2.2 Native (`native/`)

The Android app MUST use:

* Kotlin 1.9
* Jetpack Compose + Material3
* Navigation Compose
* Room + DataStore
* WorkManager
* Retrofit + OkHttp
* Auth0 Android SDK
* `MediaRecorder` / `MediaPlayer`

### 2.3 Web (`web/`)

The web app MUST use:

* React 19 + TypeScript
* Vite 6
* Auth0 React SDK
* Tailwind via CDN loaded in `index.html`
* Gemini SDK `@google/genai` in the current implementation

---

## 3. Architecture Patterns

### 3.1 Backend Architecture

Use the layered flow:

```
FastAPI Routers -> Services -> Repositories -> SQLAlchemy Async Session -> PostgreSQL
```

Rules:

* Business logic MUST live in service layer
* Routers MUST handle transport concerns only (request parsing, response mapping, auth dependency wiring)
* Repositories MUST encapsulate persistence concerns
* All DB access MUST be async via SQLAlchemy 2 and `asyncpg`
* Schema changes MUST go through Alembic
* Backend API behavior MUST be JSON-based and client-agnostic (React web and Kotlin native)

### 3.2 Native Architecture

Use MVVM with Compose-first UI:

```
Compose UI -> ViewModel -> Use Cases/Repositories -> Room/DataStore/Network
```

Rules:

* UI state MUST be unidirectional
* Long-running/background work MUST use WorkManager
* API access MUST use Retrofit + OkHttp
* Local persistence MUST use Room and DataStore for preferences/settings

### 3.3 Web Architecture

Use component-driven React architecture:

```
React Components -> Feature Hooks/Services -> API Clients
```

Rules:

* TypeScript types MUST be used for API contracts and component props
* Auth flows MUST use Auth0 React SDK
* Tailwind usage MUST rely on CDN setup in `index.html`
* Gemini integration MUST use `@google/genai`

---

## 4. Security and Authentication Baseline

* Backend authentication MUST validate Auth0-issued JWTs using JWKS-based key resolution
* Token validation MUST verify issuer, audience, expiration, and signature
* Backend authorization MUST follow RBAC with minimum baseline roles `user` and `admin`
* Authenticated identities MUST resolve to an internal user row using (`provider`, `provider_sub`)
* Domain ownership/audit foreign keys MUST reference internal user IDs, never external `sub` values
* Web and Native clients MUST obtain tokens through Auth0 SDKs (no custom auth implementation)
* Secrets MUST NOT be hardcoded; load from environment or secure platform configuration
* Authorization decisions MUST be deny-by-default

---

## 5. Quality Gates

Backend changes are not complete unless all of the following pass:

* `pytest`
* async tests via `pytest-asyncio`
* static typing checks via `mypy`

Native and web modules SHOULD define equivalent automated checks in their own toolchains, but backend gates above are mandatory for `backend/`.

---

## 6. Reference Layout

Minimum expected top-level workspace structure:

```
project-root/
  backend/
  native/
  web/
```

Each module may evolve internally, but MUST preserve the technology constraints in this document.
