# TASKS - EXAMPLES

## Phase 1: Foundation

### TASK-001: Project Skeleton and Django Configuration
- **Epic**: EPIC-1
- **Agent**: Architect-Agent
- **Status**: completed
- **Description**: Create the Django project structure, settings.py, urls.py, wsgi.py, manage.py. Configure for SQLAlchemy (not Django ORM). Set up environment-based configuration.
- **Acceptance Criteria**:
  - manage.py runs without error
  - settings.py reads from environment variables
  - Django does not use its own ORM (no DATABASES config for Django models)
  - Project structure matches FOUNDATIONS.md section 4
  - All __init__.py files created

### TASK-002: SQLAlchemy Base Model and Database Setup
- **Epic**: EPIC-1
- **Agent**: Data-Agent
- **Status**: completed
- **Depends On**: TASK-001
- **Description**: Create core/db.py with SQLAlchemy engine and session factory. Create core/base_model.py with BaseModel (id UUID, created_at, updated_at, deleted_at). Configure for PostgreSQL.
- **Acceptance Criteria**:
  - BaseModel has id (UUID), created_at, updated_at, deleted_at
  - Session factory creates scoped sessions
  - Engine reads DATABASE_URL from environment
  - Follows DB_SCHEMA.md conventions

### TASK-003: SQLAlchemy User Model
- **Epic**: EPIC-2
- **Agent**: Data-Agent
- **Status**: completed
- **Depends On**: TASK-002
- **Description**: Create accounts/models.py with User SQLAlchemy model. Fields: id, auth0_sub (unique), email (unique), name, role (enum: author/admin), is_active, last_login_at, created_at, updated_at, deleted_at.
- **Acceptance Criteria**:
  - User model inherits BaseModel
  - role uses PostgreSQL ENUM (author, admin)
  - auth0_sub has unique constraint
  - email has unique constraint
  - Indexes on auth0_sub, email, role

### TASK-004: SQLAlchemy Article, Category, Tag Models
- **Epic**: EPIC-3
- **Agent**: Data-Agent
- **Status**: completed
- **Depends On**: TASK-002
- **Description**: Create articles/models.py with Article, Category, Tag models and article_tags association table. Article has SEO fields: meta_title, meta_description, og_image, canonical_url, slug (unique). Status enum: draft, pending_review, approved, rejected.
- **Acceptance Criteria**:
  - Article model has all required fields including SEO
  - Status is PostgreSQL ENUM
  - slug is unique
  - author_id FK to users
  - category_id FK to categories (nullable)
  - article_tags association table with audit fields
  - Proper indexes on slug, status, author_id, created_at

### TASK-005: Alembic Configuration and Initial Migration
- **Epic**: EPIC-1
- **Agent**: Data-Agent
- **Status**: completed
- **Depends On**: TASK-003, TASK-004
- **Description**: Set up Alembic with alembic.ini and env.py. Generate initial migration for all tables (users, articles, categories, tags, article_tags).
- **Acceptance Criteria**:
  - alembic.ini configured for PostgreSQL
  - env.py imports all SQLAlchemy models
  - Initial migration creates all tables with constraints and indexes
  - Migration is reversible

### TASK-006: Pydantic Schemas
- **Epic**: EPIC-3
- **Agent**: Data-Agent
- **Status**: completed
- **Depends On**: TASK-003, TASK-004
- **Description**: Create accounts/schemas.py and articles/schemas.py with Pydantic v2 models for validation and serialization.
- **Acceptance Criteria**:
  - UserRead schema
  - ArticleCreate, ArticleUpdate, ArticleRead schemas
  - SEO fields included in article schemas
  - Proper validation rules (slug format, required fields)

### TASK-007: Domain Exceptions
- **Epic**: EPIC-1
- **Agent**: Architect-Agent
- **Status**: completed
- **Description**: Create core/exceptions.py with domain exception classes: NotFoundError, PermissionDeniedError, ValidationError, ConflictError.
- **Acceptance Criteria**:
  - All exceptions inherit from a base DomainError
  - Each has a message and optional code
  - No framework-specific imports

## Phase 2: Backend

### TASK-008: Auth0 Integration (Login, Callback, Logout)
- **Epic**: EPIC-2
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-003, TASK-005
- **Description**: Implement Auth0 OIDC flow using Authlib. Create accounts/auth0_backend.py, accounts/views.py (login, callback, logout), accounts/urls.py. On callback: upsert user, store in session.
- **Acceptance Criteria**:
  - Login redirects to Auth0 Universal Login
  - Callback exchanges code, validates tokens, upserts user
  - Session stores user_id, email, name, role, auth0_sub
  - Logout clears session and redirects to Auth0 logout
  - Configuration via environment variables
  - Tests for callback logic and user upsert

### TASK-009: Session Middleware
- **Epic**: EPIC-2
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-008
- **Description**: Create accounts/middleware.py that attaches user info from session to request object for easy access in views.
- **Acceptance Criteria**:
  - Middleware reads session and sets request.current_user
  - If no session, request.current_user is None
  - No DB queries in middleware (reads from session only)

### TASK-010: Article Services (Business Logic)
- **Epic**: EPIC-3
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-004, TASK-006
- **Description**: Create articles/services.py with ArticleService class. Implements: create_article, update_article, get_article_by_slug, list_published_articles, list_user_articles, submit_for_review, approve_article, reject_article.
- **Acceptance Criteria**:
  - Non-admin creates article with status=draft
  - Admin creates article with status=approved
  - Non-admin update resets status to draft
  - submit_for_review changes status from draft to pending_review
  - approve_article changes from pending_review to approved (admin only)
  - reject_article changes from pending_review to rejected (admin only)
  - Editing approved article resets to draft
  - All operations use Pydantic validation
  - Structured logging on all operations
  - Tests for all business rules

### TASK-011: Article Views (CRUD + Admin)
- **Epic**: EPIC-3
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-010, TASK-009
- **Description**: Create articles/views.py and articles/urls.py. Views: home (public listing), article detail, create, edit, admin review queue, approve, reject.
- **Acceptance Criteria**:
  - Home page shows only approved articles, paginated
  - Article detail shows only approved articles (or own drafts)
  - Create requires authentication
  - Edit requires authentication + ownership (or admin)
  - Admin review requires admin role
  - CSRF on all POST
  - Proper redirects and error handling

### TASK-012: Account Services
- **Epic**: EPIC-2
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-003, TASK-006
- **Description**: Create accounts/services.py with UserService class for user upsert and lookup operations.
- **Acceptance Criteria**:
  - upsert_user: create or update user by auth0_sub
  - get_user_by_id: lookup user
  - New users default to role=author
  - Tests for upsert logic

## Phase 3: Frontend (Templates)

### TASK-013: Base Template with TailwindCSS
- **Epic**: EPIC-6
- **Agent**: Frontend-Agent
- **Status**: completed
- **Depends On**: TASK-001
- **Description**: Create templates/base.html with TailwindCSS CDN (or compiled), navigation, footer, SEO meta block, flash messages block.
- **Acceptance Criteria**:
  - Clean, modern design
  - Responsive navigation with login/logout
  - SEO meta block (overridable by child templates)
  - Flash/message display area
  - Proper semantic HTML
  - Accessible (keyboard nav, contrast)

### TASK-014: Home Page Template
- **Epic**: EPIC-6
- **Agent**: Frontend-Agent
- **Status**: completed
- **Depends On**: TASK-013, TASK-011
- **Description**: Create templates/home.html showing published articles with pagination.
- **Acceptance Criteria**:
  - Article cards with title, excerpt, author, date
  - Pagination controls
  - Empty state when no articles
  - SEO meta tags for home page

### TASK-015: Article Detail Template
- **Epic**: EPIC-6
- **Agent**: Frontend-Agent
- **Status**: completed
- **Depends On**: TASK-013, TASK-011
- **Description**: Create templates/articles/detail.html with full article content, SEO meta tags, Open Graph, JSON-LD structured data.
- **Acceptance Criteria**:
  - Full article rendering (title, body, author, date, tags, category)
  - meta title, meta description in head
  - Open Graph tags (og:title, og:description, og:image, og:url, og:type)
  - canonical URL
  - JSON-LD Article structured data
  - Clean typography

### TASK-016: Article Create/Edit Forms
- **Epic**: EPIC-7
- **Agent**: Frontend-Agent
- **Status**: completed
- **Depends On**: TASK-013, TASK-011
- **Description**: Create templates/articles/create.html and edit.html with forms for article content and SEO fields.
- **Acceptance Criteria**:
  - Form fields: title, slug, body, category, tags, meta_title, meta_description, og_image, canonical_url
  - Client-side validation
  - CSRF token included
  - Error display for validation failures
  - Disabled submit while processing

### TASK-017: Author Dashboard Template
- **Epic**: EPIC-7
- **Agent**: Frontend-Agent
- **Status**: completed
- **Depends On**: TASK-013, TASK-011
- **Description**: Create templates/accounts/dashboard.html showing user's articles with status, edit/delete actions.
- **Acceptance Criteria**:
  - List of user's articles with status badges
  - Create new article button
  - Edit link for draft/rejected articles
  - Submit for review action
  - Empty state

### TASK-018: Admin Review Template
- **Epic**: EPIC-8
- **Agent**: Frontend-Agent
- **Status**: completed
- **Depends On**: TASK-013, TASK-011
- **Description**: Create templates/articles/admin_review.html showing pending articles with approve/reject actions.
- **Acceptance Criteria**:
  - List of pending_review articles
  - Preview link
  - Approve/reject buttons with confirmation
  - Empty state when no pending articles

## Phase 4: DevOps

### TASK-019: Requirements File
- **Epic**: EPIC-1
- **Agent**: Devops-Agent
- **Status**: completed
- **Description**: Create requirements.txt with all production dependencies pinned.
- **Acceptance Criteria**:
  - Django, SQLAlchemy, Alembic, Pydantic, Authlib, psycopg2-binary, gunicorn, uvicorn
  - Versions pinned
  - No unnecessary dependencies

### TASK-020: Dockerfile
- **Epic**: EPIC-1
- **Agent**: Devops-Agent
- **Status**: completed
- **Depends On**: TASK-019
- **Description**: Create multi-stage Dockerfile for production deployment.
- **Acceptance Criteria**:
  - Multi-stage build (builder + runtime)
  - Non-root user
  - Static files collected
  - Gunicorn as entrypoint
  - Health check
  - .dockerignore included

### TASK-021: Kubernetes Manifests
- **Epic**: EPIC-1
- **Agent**: Devops-Agent
- **Status**: completed
- **Depends On**: TASK-020
- **Description**: Create k8s/ directory with deployment.yaml, service.yaml, ingress.yaml, configmap.yaml.
- **Acceptance Criteria**:
  - Deployment with resource limits, liveness/readiness probes
  - Service (ClusterIP)
  - Ingress with TLS
  - ConfigMap for non-secret env vars
  - Secret references for sensitive vars (AUTH0_CLIENT_SECRET, DATABASE_URL)
  - Rolling update strategy

## Phase 5: Testing

### TASK-022: Test Configuration and Fixtures
- **Epic**: EPIC-11
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-005
- **Description**: Create tests/conftest.py with pytest fixtures for test database, SQLAlchemy sessions, test users, test articles.
- **Acceptance Criteria**:
  - Test database setup/teardown
  - Session fixture with transaction rollback
  - Factory fixtures for users and articles
  - Django test client configured

### TASK-023: Account Tests
- **Epic**: EPIC-11
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-022, TASK-008, TASK-012
- **Description**: Write tests for Auth0 callback, user upsert, session management.
- **Acceptance Criteria**:
  - Test user creation on first login
  - Test user update on subsequent login
  - Test session population
  - Test logout clears session
  - Test unauthenticated access blocked

### TASK-024: Article Tests
- **Epic**: EPIC-11
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-022, TASK-010, TASK-011
- **Description**: Write tests for article CRUD, role-based workflow, SEO fields.
- **Acceptance Criteria**:
  - Test create article as author (status=draft)
  - Test create article as admin (status=approved)
  - Test update by non-admin resets to draft
  - Test submit for review
  - Test approve/reject (admin only)
  - Test public listing shows only approved
  - Test SEO fields saved and rendered
  - Test authorization enforcement

### TASK-025: Structured Logging Setup
- **Epic**: EPIC-10
- **Agent**: Backend-Agent
- **Status**: completed
- **Depends On**: TASK-001
- **Description**: Create core/logging.py with structured JSON logging configuration.
- **Acceptance Criteria**:
  - JSON format logs
  - Log level configurable via environment
  - Request ID in logs
  - No sensitive data logged
