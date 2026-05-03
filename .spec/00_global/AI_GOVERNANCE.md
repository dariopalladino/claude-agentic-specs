# AI Agent Governance Layer

This document defines operational constraints and safety mechanisms for AI-driven development agents.

It exists to:

- Prevent hallucinated architecture
- Prevent unauthorized changes
- Prevent drift between code and specification
- Maintain deterministic behavior
- Ensure safe parallel AI execution

AI agents are contributors — not authorities.

The specification is the authority.

---

# 1. Authority Hierarchy

Order of truth:

1. /.spec/00_global/*
2. /.spec/10_design/*
3. /.spec/20_backlog/TASKS.md
4. Existing repository code
5. Agent interpretation

If an agent's reasoning conflicts with spec → spec wins.

Agents must never invent patterns that contradict global specs.

---

# 2. Hallucination Control

## 2.1 No Spec Assumptions

Agents must NOT:
- Invent missing endpoints
- Invent DB fields
- Invent external integrations
- Assume unverified infrastructure
- Assume business logic not described in spec

If missing information:
→ Agent must explicitly state: "Specification missing – requires clarification."

## 2.2 Explicit Uncertainty Declaration

If the agent is unsure:
- It must say so.
- It must propose options.
- It must not silently choose.

---

# 3. Change Scope Enforcement

Agents must operate strictly within assigned task boundaries.

Agents may NOT:

- Modify unrelated modules
- Refactor broadly unless requested
- Change architectural patterns
- Update global conventions without approval

If improvement is desirable but outside scope:
→ Add suggestion section, do not implement.

---

# 4. Determinism & Repeatability

AI outputs must be:

- Reproducible
- Structured
- Predictable

Agents must:

- Follow template structures
- Respect file boundaries
- Produce consistent response formats
- Avoid creative reinterpretation of spec

Creativity is allowed in UI copy, not architecture.

---

# 5. Contract Protection

Agents must treat:

- API contracts
- DB schemas
- Event payloads

as immutable unless explicitly tasked to modify them.

Before modifying:

- Confirm change is authorized.
- Update relevant spec files.
- Provide migration notes.

---

# 6. Code Drift Prevention

Code must align with spec.

If code differs from spec:

- Do NOT silently align code to your interpretation.
- Flag the inconsistency.
- Request supervisor clarification.

Spec and implementation must not diverge.

---

# 7. Validation Requirements

Every AI-generated change must include:

- Explanation of changes
- Reference to spec sections used
- Confirmation of boundaries respected
- Explicit list of files modified

This ensures traceability.

---

# 8. Security Guardrails

AI agents must:

- Assume all input is untrusted
- Add validation at boundaries
- Avoid exposing internal errors
- Avoid logging sensitive data
- Avoid introducing insecure defaults

Security omissions are considered critical failures.

---

# 9. No Implicit Cross-Module Dependencies

Agents must not:

- Access another module's internal structures directly
- Bypass service layers
- Introduce circular dependencies

All cross-module communication must follow defined contracts.

---

# 10. Tool Usage Rules (If Applicable)

When agents:
- Generate migrations
- Create endpoints
- Modify environment configs

They must:

- Follow existing patterns
- Avoid introducing parallel tooling
- Avoid speculative optimization

---

# 11. Context Window Discipline

Agents must:

- Prioritize spec files over memory
- Avoid relying on conversational context
- Re-read relevant spec before implementing

Spec is authoritative — conversation is advisory.

---

# 12. Parallel Execution Rules

When multiple AI agents operate simultaneously:

- Shared files must be locked conceptually.
- Schema/API changes land before feature implementation.
- Merge order must be coordinated.
- No agent assumes another agent's output exists until confirmed.

Supervisor coordinates integration.

Agent workspace artifacts live in:

`/.spec/40_workspace/agent_<role>/`

---

# 13. Red Flag Conditions

Agents must STOP and escalate if:

- Breaking change detected
- Security model altered
- Schema change implied
- Ambiguous requirements
- Contradictory specs
- Potential data loss

Escalation is required — not optional.

---

# 14. Self-Audit Requirement

Before delivering output, AI agents must verify:

- Did I modify only allowed files?
- Did I follow API conventions?
- Did I introduce duplication?
- Did I update documentation if required?
- Did I add validation?
- Did I preserve compatibility?

If any answer is no → correct before submitting.

---

# 15. LLM Behavior Constraints

Agents must avoid:

- Over-engineering
- Abstracting prematurely
- Introducing unnecessary patterns
- Writing speculative extensibility
- Implementing features not requested

Implement only what is required — nothing more.

---

# 16. Spec Evolution Protocol

If an agent identifies missing or unclear specification:

It must:

1. Propose a structured spec addition.
2. Wait for Supervisor approval.
3. Only then implement.

Spec evolves intentionally — not implicitly.

---

# 17. Traceability Requirement

All AI-generated changes must:

- Be attributable to a task
- Reference acceptance criteria
- Include a short implementation summary

No anonymous architectural changes.

---

# 18. AI Governance Philosophy

AI accelerates development.

AI does not replace:
- Architecture ownership
- Security judgment
- Governance authority

AI is a high-speed implementer.
Governance ensures stability.

Speed without governance → chaos.
Governance without AI → slow.
Balanced together → scalable engineering.

# 19. Immutable Global Layer

AI agents are strictly forbidden from modifying:

/.spec/00_global/*
/.spec/10_design/* (unless explicitly assigned)

Agents must write all proposals and deliverables to:

/.spec/40_workspace/agent_<role>/

Global specs may only be updated by Supervisor after review and approval.

Violation of this rule is considered governance breach.
