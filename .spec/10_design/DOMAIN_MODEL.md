# Core Principles
## 1. Domain-First Modeling

The domain model represents business concepts, not database tables.

Entities must:
- Reflect business meaning
- Have explicit invariants
- Avoid infrastructure leakage

Bad:
- UserTable
- OrderRow

Good:
- User
- Order
- Subscription
- Invoice

## 2. Separation of Layers

The system must maintain strict separation between:

- Domain entities
- Persistence models
- API schemas

Never mix responsibilities.

| Layer	 | Technology	 | Responsibility  |
|--------|---------------|-----------------|
| API	| Pydantic	| Request/Response schemas |
| Domain	| Python classes	| Business logic + invariants |
| Persistence	| SQLAlchemy	| Database mapping |

Domain entities must not depend on:
- FastAPI
- SQLAlchemy
- Alembic

## Canonical Entity Rules
### BaseEntity

All entities must inherit from BaseEntity.

Required fields:

id: UUID
created_at: datetime
updated_at: datetime
is_deleted: bool

Rules:

- IDs must be UUID v7 or UUID v4
- Timestamps must be UTC
- Soft delete is mandatory
- updated_at must change on mutation

### Entity Identity

Entities are compared by identity only.
entity_a.id == entity_b.id

Never compare by attributes.

### Mutability Rules

Entities are mutable only through explicit methods.

Bad:
user.email = "new@email.com"

Good:
user.change_email("new@email.com")

### Invariant Enforcement

All invariants must be enforced inside the entity.

Example:

class User(BaseEntity):
    def change_email(self, email: str):
        if "@" not in email:
            raise DomainError("Invalid email")
        self.email = email

Never rely on:
- API validation
- Database constraints
- Service layer checks