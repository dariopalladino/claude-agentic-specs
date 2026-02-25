# Foundation

This document defines **mandatory conventions** that must be followed when building the blog application backend.

The goal is to ensure:

* Consistent behavior across the application
* Predictable error handling
* Observability
* Security compliance
* Server-side rendered HTML for SEO
* Long-term maintainability

All implementation must comply with this specification.

---

## 1. Framework Decision

**Django** is the chosen framework (not FastAPI).

Rationale:
- Server-side rendered HTML ensures all SEO meta tags are in the initial HTML response
- No need for a separate API layer + SPA hydration complexity
- Simpler deployment (one service instead of two)
- TailwindCSS works with Django templates
- Auth0 integration is straightforward with Django
- Python backend was already required

---

## 2. Technology Baseline

All services MUST use:

* Python 3.11+
* Django 5.x
* SQLAlchemy 2.x (for database models and queries)
* Alembic (for migrations)
* Pydantic v2 (for validation/serialization)
* Gunicorn + Uvicorn workers (production)
* Authlib (for Auth0 OIDC integration)
* TailwindCSS (for frontend styling)
* Structured logging (JSON)
* PostgreSQL

Optional but recommended:

* django-extensions
* whitenoise (static files in production)

---

## 3. Architecture Pattern

Django is used for:
- URL routing
- Views (request handling)
- Template rendering (SSR)
- Session management
- Middleware

SQLAlchemy is used for:
- Database model definitions (source of truth)
- Database queries via sessions
- Relationship definitions

Alembic is used for:
- All schema migrations

Pydantic is used for:
- Form validation
- Data serialization
- Request/response contracts

Django ORM is NOT used. All database access goes through SQLAlchemy sessions.

---

## 4. Required Project Structure

```
blog/
  app/
    manage.py
    requirements.txt
    Dockerfile
    k8s/
      deployment.yaml
      service.yaml
      ingress.yaml
      configmap.yaml
    alembic/
      alembic.ini
      env.py
      versions/
    config/
      __init__.py
      settings.py
      urls.py
      wsgi.py
    accounts/
      __init__.py
      models.py          # SQLAlchemy User model
      auth0_backend.py   # Auth0 authentication backend
      middleware.py       # Session middleware for user details
      views.py           # Login/logout/callback views
      urls.py
      services.py        # Business logic for user operations
      schemas.py         # Pydantic schemas for user data
    articles/
      __init__.py
      models.py          # SQLAlchemy Article, Category, Tag models with SEO fields
      views.py           # CRUD views with role-based logic
      forms.py           # Django forms backed by Pydantic validation
      services.py        # Business logic for article operations
      schemas.py         # Pydantic schemas for article data
      urls.py
    core/
      __init__.py
      db.py              # SQLAlchemy engine, session factory
      base_model.py      # Base SQLAlchemy model with audit fields
      exceptions.py      # Domain exceptions
      logging.py         # Structured logging setup
    templates/
      base.html          # Base template with TailwindCSS + SEO meta
      home.html
      accounts/
        login.html
        dashboard.html
      articles/
        list.html
        detail.html
        create.html
        edit.html
        admin_review.html
    static/
      css/
        input.css        # TailwindCSS input
      js/
    tests/
      __init__.py
      conftest.py
      test_accounts/
      test_articles/
```

---

## 5. Layered Architecture

```
Django Views (presentation) -> Services (business logic) -> Repositories/SQLAlchemy (data access)
```

Business rules must not live in:
- Views/URL handlers
- Templates
- Migration scripts

Views handle:
- Request parsing
- Authentication/authorization checks
- Delegating to services
- Rendering templates

Services handle:
- Business logic
- Validation via Pydantic
- Orchestrating repository calls

Data access:
- SQLAlchemy sessions and queries
- No raw SQL unless justified

---

## 6. SEO Requirements

Every article page MUST render in the initial HTML:
- meta title
- meta description
- canonical URL
- Open Graph tags (og:title, og:description, og:image, og:url, og:type)
- JSON-LD structured data (Article schema)

The base template must include a block for SEO meta tags that child templates override.

---

## 7. Session Management

Auth0 integration uses backend-for-frontend pattern:
- On successful Auth0 callback, user details are stored in Django session
- Session contains: user_id, email, name, role, auth0_sub
- Subsequent requests read from session (no DB query per request)
- Session cookie is httpOnly, secure, SameSite=Lax
