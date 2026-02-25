# Governance

This document defines how architectural integrity, quality, and change control are maintained across the project.

Governance exists to:
- Prevent architectural drift
- Protect security posture
- Ensure long-term maintainability
- Enable parallel work safely

---

# 1. Authority Model

## 1.1 Supervisor Authority

The Supervisor (or Lead Architect) has final authority over:

- Architecture changes
- DB schema changes
- API contract changes
- Security posture changes
- Cross-module refactors
- Breaking changes

No agent or developer may override these without approval.

---

# 2. Change Control

## 2.1 Allowed Without Approval

The following changes do NOT require special approval:

- Bug fixes
- UI improvements within conventions
- Refactoring without behavior change
- Internal implementation improvements
- Adding tests
- Documentation updates

## 2.2 Requires Approval

The following require Supervisor approval:

- Database schema modifications
- API contract changes
- Authentication/authorization logic changes
- New infrastructure dependencies
- Breaking changes
- Introduction of new architectural patterns
- Cross-cutting library additions

---

# 3. Schema Governance

## 3.1 Database Changes

Any schema change must include:

- Migration plan
- Rollback plan
- Backward compatibility strategy (if applicable)
- Performance consideration (indexes, constraints)
- Updated DB_SCHEMA.md

No direct DB modifications in production without versioned migration.

---

# 4. API Governance

## 4.1 Contract Stability

- Public API contracts must be versioned if breaking.
- Response shape changes require review.
- Removal of fields requires deprecation period.

## 4.2 Deprecation Policy

- Deprecate first.
- Provide migration guidance.
- Remove only after agreed window.

---

# 5. Security Governance

Security-sensitive changes require explicit review.

Includes:
- Auth changes
- Role/permission changes
- External integrations
- Data exposure changes

Security reviews must verify:
- Input validation
- Authorization enforcement
- Logging hygiene
- Secrets management

---

# 6. Versioning Policy

## 6.1 Semantic Versioning (Recommended)

MAJOR: Breaking changes  
MINOR: Backward-compatible feature additions  
PATCH: Bug fixes  

---

# 7. Release Process

Minimum release requirements:

- Tests passing
- No critical linting issues
- Security review completed
- Performance baseline validated (if affected)
- Documentation updated

Production releases must:
- Be traceable to version tag
- Include migration steps (if needed)

---

# 8. Documentation Requirements

Any change that affects:

- Architecture
- API contract
- Schema
- Security model

Must update corresponding spec files before approval.

Specs are not optional.

If code and spec diverge → spec must be corrected or code reverted.

---

# 9. Observability Requirements

New critical features must include:

- Logging
- Error visibility
- Traceability for debugging

No silent background failures.

---

# 10. Risk Escalation

If a change introduces:

- Data loss risk
- Downtime risk
- Security exposure
- Significant refactor

Supervisor must pause feature work and evaluate impact before proceeding.

---

# 11. Parallel Development Rules

When multiple agents work in parallel:

- File ownership must be clear.
- Shared files require coordination.
- Schema/API changes must land first.
- Avoid speculative refactors.
- Avoid duplicate utility creation.

Supervisor coordinates merge order.

---

# 12. Breaking Change Protocol

When a breaking change is necessary:

1. Document rationale.
2. Define migration path.
3. Update version.
4. Notify stakeholders.
5. Ensure rollback capability.

Breaking changes without protocol are not allowed.

---

# 13. Technical Debt Management

Technical debt must be:

- Documented
- Scoped
- Scheduled

No hidden debt accumulation.

---

# 14. Emergency Changes

In case of production emergency:

- Patch may bypass normal flow.
- Must be reviewed post-incident.
- Must update documentation afterward.

Emergency does not remove governance — it delays it.

---

# 15. Governance Philosophy

Governance is not bureaucracy.

It exists to:

- Enable safe speed
- Protect long-term quality
- Avoid chaos under parallel development
- Maintain system integrity

If governance slows progress, improve the process — do not bypass it.
