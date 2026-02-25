# DB_SCHEMA.md

Database Schema Design Specification
Stack: FastAPI + SQLAlchemy + Pydantic + Alembic + PostgreSQL with pgvector

---

# Purpose

This document defines the **database modeling standards** that MUST be followed when generating or modifying database schemas.

All assistants and developers must comply with these rules to ensure:

* Consistency
* Migration safety
* Predictable ORM behavior
* Long-term maintainability
* Production-grade schema evolution

This specification is **authoritative**.

---

# Core Principles

## 1. Database First

The relational schema is the source of truth.

Always define:

* Tables
* Constraints
* Relationships
* Indexes
* Naming conventions

before implementing application logic.

---

## 2. PostgreSQL Assumption

Schemas must be designed for PostgreSQL.

Use:

* UUID primary keys
* JSONB where appropriate
* TIMESTAMP WITH TIME ZONE
* Constraints and indexes explicitly

Avoid DB-agnostic compromises.

---

## 3. Migrations Are Mandatory

All schema changes must be done via Alembic migrations.

Never:

* Recreate tables
* Modify tables manually
* Use `Base.metadata.create_all()` in production

---

# Naming Conventions

## Tables

Tables must be plural, snake_case.

Examples:

```
users
orders
audio_files
lesson_modules
```

---

## Columns

Columns must be snake_case.

Examples:

```
created_at
updated_at
user_id
is_active
```

---

## Primary Keys

All tables must use:

```
id UUID PRIMARY KEY
```

SQLAlchemy:

```
id = Column(UUID(as_uuid=True), primary_key=True, default=uuid4)
```

---

## Foreign Keys

Foreign keys must follow:

```
<entity>_id
```

Examples:

```
user_id
order_id
audio_file_id
```

Always define FK constraints.

---

## Index Naming

Indexes must follow:

```
ix_<table>_<column>
```

Unique indexes:

```
uq_<table>_<column>
```

Foreign key constraints:

```
fk_<table>_<column>_<target_table>
```

---

# Required Columns (Audit Fields)

Every table MUST include:

```
created_at TIMESTAMPTZ NOT NULL
updated_at TIMESTAMPTZ NOT NULL
```

Optional but recommended:

```
deleted_at TIMESTAMPTZ NULL
```

---

# Timestamp Behavior

## created_at

Set once at insert.

## updated_at

Updated on every write.

Handled at application layer.

---

# Soft Deletes

Never hard-delete business entities.

Use:

```
deleted_at TIMESTAMPTZ NULL
```

Soft delete rule:

```
deleted_at IS NULL => active row
```

---

# Relationship Rules

## Explicit Relationships

All relationships must define:

* Foreign key constraint
* SQLAlchemy relationship()
* Back reference

Example:

```
User -> Orders (1:N)
```

---

## No Implicit Join Tables

Many-to-many relationships must use explicit association tables.

Example:

```
user_roles
lesson_tags
```

Association tables must have:

* Primary key
* Audit fields

---

# Normalization Rules

Minimum requirement: **3NF**

Avoid:

* Repeated columns
* Comma-separated values
* JSON storage for relational data

JSONB is allowed only for:

* Metadata
* Config blobs
* Non-relational payloads

---

# Enum Strategy

Prefer PostgreSQL ENUM types for stable values.

Examples:

* user_role
* order_status
* subscription_tier

Avoid string status columns without constraints.

---

# Boolean Columns

Boolean columns must be named as predicates:

Good:

```
is_active
is_verified
has_subscription
```

Bad:

```
active
verified
subscription
```

---

# Unique Constraints

Always enforce uniqueness at DB level.

Examples:

```
email UNIQUE
provider_user_id UNIQUE
```

Never rely on application-only validation.

---

# Index Strategy

Indexes must exist for:

* Foreign keys
* Frequently filtered columns
* Unique fields
* Time-based queries

Example:

```
created_at
user_id
status
```

---

# Migration Safety Rules

Migrations must be:

* Reversible
* Deterministic
* Backward compatible when possible

Never:

* Drop columns without migration path
* Rename without migration script
* Change types destructively

---

# SQLAlchemy Model Rules

Each model must:

* Inherit from Base
* Define **tablename**
* Use typed columns
* Define relationships explicitly

---

# Pydantic Schema Rules

Separate schemas into:

```
EntityBase
EntityCreate
EntityUpdate
EntityRead
```

Never expose internal DB fields unintentionally.

---

# Example Table Template

```
id UUID PRIMARY KEY
created_at TIMESTAMPTZ NOT NULL
updated_at TIMESTAMPTZ NOT NULL
deleted_at TIMESTAMPTZ NULL
```

---

# Example Entity Pattern

User:

```
users
-----
id
email (unique)
password_hash
is_active
created_at
updated_at
deleted_at
```

Order:

```
orders
------
id
user_id (FK)
status
created_at
updated_at
```

---

# Anti-Patterns (Forbidden)

## No integer autoincrement IDs

## No missing audit fields

## No schema changes without migration

## No implicit relationships

## No JSON replacing relational modeling

## No business logic in migrations

## No cascade deletes on core entities

---

# Alembic Requirements

Autogenerate is allowed, but migrations must be reviewed.

Each migration must:

* Have meaningful message
* Include indexes
* Include constraints
* Preserve data

Example message:

```
add_user_subscription_table
```

---

# Versioning Rule

Schema evolves incrementally.
Never rebuild schema from scratch in production.

---

# Final Rule

When unsure:

* Prefer explicit over implicit
* Prefer constraints over assumptions
* Prefer migrations over manual fixes
* Prefer normalization over shortcuts

```
```
