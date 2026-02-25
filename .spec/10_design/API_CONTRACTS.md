# API_CONTRACTS.md

# 1. Guiding Principles

All views must be:

* Deterministic
* Typed (via Pydantic schemas for validation)
* Domain-organized
* Testable
* Observable
* Secure by default

Views must **never contain business logic**.
They should only orchestrate dependencies.

Layer responsibilities:

```
Django View -> Service -> SQLAlchemy Query -> Database
```

---

# 2. Project Structure Contract

All endpoints follow the Django project structure defined in FOUNDATIONS.md:

```
config/urls.py          # Root URL configuration
accounts/urls.py        # Auth URLs (login, callback, logout)
accounts/views.py       # Auth views
articles/urls.py        # Article URLs (CRUD, admin)
articles/views.py       # Article views
```

---

# 3. URL Naming Rules

## URL Patterns

Use plural nouns, clean paths.

```
GET  /                          # Home (public article listing)
GET  /articles/<slug>/          # Article detail (public, approved only)
GET  /dashboard/                # Author dashboard
GET  /dashboard/articles/new/   # Create article form
POST /dashboard/articles/new/   # Submit new article
GET  /dashboard/articles/<id>/edit/  # Edit article form
POST /dashboard/articles/<id>/edit/  # Submit article edit
GET  /admin/review/             # Admin review queue
POST /admin/review/<id>/approve/  # Approve article
POST /admin/review/<id>/reject/   # Reject article
GET  /auth/login/               # Initiate Auth0 login
GET  /auth/callback/            # Auth0 callback
GET  /auth/logout/              # Logout
```

---

# 4. View Contract

Each Django app has its own views.py and urls.py.

View rules:
* No DB access in views directly (use services)
* No business logic in views
* Only request parsing + delegation + template rendering
* All POST views require CSRF protection

---

# 5. Service Layer Contract

The service layer contains business logic.

Rules:
* No Django imports (HTTP, request objects)
* Pure Python logic
* Fully unit testable
* Receives Pydantic schemas or plain data, returns domain objects

---

# 6. Data Access Contract

SQLAlchemy sessions are used for all database access.

Rules:
* Only persistence logic
* No business logic
* SQLAlchemy only
* Sessions obtained from core/db.py session factory

---

# 7. Schema Contract (Pydantic)

Each domain must define:

```
ArticleCreate
ArticleUpdate
ArticleRead
UserRead
```

Never expose internal DB fields unintentionally.

---

# 8. Error Handling Contract

Use domain exceptions in core/exceptions.py.

Views catch domain exceptions and render appropriate error pages or redirect with messages.

---

# 9. Authorization Contract

All protected views must check:
- User is authenticated (via session)
- User has required role
- User owns the resource (where applicable)

Authorization is enforced server-side via decorators or middleware.

---

# 10. Pagination Contract

List views must support pagination.

Default: 20 items per page.
Use Django-style page-based pagination with SQLAlchemy queries.

---

# 11. Testing Contract

Every view requires:
* View test (integration)
* Service test (unit)

Minimum coverage target: 80%

---

# 12. Security Contract

All non-public views must verify authenticated session.
CSRF protection on all POST/PUT/DELETE.
Never trust client-provided user IDs.

---

# End of Contract
