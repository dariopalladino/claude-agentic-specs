# Authorization Model

This specification defines mandatory authorization behavior based on OpenID Connect (OIDC) authentication and role-based access control (RBAC).

All services MUST enforce authorization server-side and MUST use a persistent user table to map identity provider users to internal records.

---

## 1. Identity and Authentication (OIDC)

Authentication MUST rely on OIDC-compliant identity providers.

Required behavior:
- Validate ID/access tokens according to OIDC and JWT best practices.
- Validate issuer (`iss`), audience (`aud`), expiration (`exp`), and signature.
- Resolve signing keys via JWKS.
- Treat identity provider subject (`sub`) as the canonical external identity.

---

## 2. RBAC Model

Authorization MUST use RBAC.

Minimum role set:
- `user`
- `admin`

This minimum role set is mandatory across backend APIs unless explicitly extended by domain-specific policy.

Projects may define additional roles, but all roles MUST be explicitly documented and enforced in server-side policies.

Role assignment source of truth:
- Persisted in application database (preferred), or
- Deterministically derived from verified identity provider claims.

Frontend role checks are convenience-only and MUST NOT be considered security enforcement.

---

## 3. User Identity Table (Mandatory)

A user table MUST exist to track identity provider users and bind platform identity to internal domain data.

Minimum required fields:
- `id` (internal primary key)
- `provider` (identity provider name)
- `provider_sub` (OIDC `sub`, unique per provider)
- `email` (nullable if not provided by IdP)
- `display_name` (nullable)
- `role` (or normalized role relation)
- `created_at`
- `updated_at`

Constraints:
- Unique constraint on (`provider`, `provider_sub`).
- Internal `id` MUST be stable and immutable once created.

Provisioning behavior:
- On first successful authentication, create user record if missing.
- On subsequent logins, resolve and reuse existing user record.

---

## 4. Foreign Key Policy (Mandatory)

Any schema that represents ownership, auditability, or actor attribution MUST reference the user table via foreign keys.

Required patterns:
- `created_by_user_id` -> FK to user table
- `updated_by_user_id` -> FK to user table (when applicable)
- `owner_user_id` -> FK to user table (for owned resources)

Direct foreign keys to identity provider subject values are not allowed.

---

## 5. Access Enforcement Requirements

All non-public API endpoints MUST:
- Validate authenticated user
- Resolve internal user record from verified identity (`provider` + `sub`)
- Validate role permissions
- Validate resource ownership when operation is ownership-scoped

Authorization decisions MUST be deny-by-default.

---

## 6. Implementation Rules

Authorization logic MUST be centralized in backend policy/dependency/service layers.

Authorization logic MUST NOT rely on:
- Client-provided role fields
- Client-side route guards
- UI visibility rules

Services SHOULD emit structured audit logs for authorization failures and privileged actions.
