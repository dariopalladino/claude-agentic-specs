# Core Engineering Principles

## 1. Separation of Concerns
Business logic, infrastructure, UI, and data access must be clearly separated.

## 2. Single Source of Truth
- API contracts are authoritative.
- Schema definitions are authoritative.
- No duplicated logic across layers.

## 3. Explicit Contracts
All interfaces (API, events, DB schemas) must be clearly defined and versionable.

## 4. Backward Compatibility First
Breaking changes require:
- Versioning strategy
- Migration plan
- Supervisor approval

## 5. Security by Default
- All endpoints assume authentication unless explicitly public.
- Input is never trusted.
- Authorization is explicit, not implicit.

## 6. Observability as a Feature
Logging, metrics, and tracing are part of the implementation — not an afterthought.

## 7. Test What Matters
- Core flows must be tested.
- Critical logic must not rely on manual verification.
- Tests must be deterministic.

## 8. Idempotency & Predictability
APIs should be idempotent where applicable.
Side effects must be controlled and documented.
