# Agentic Spec-Driven Web Application Framework

This repository provides a governance-first framework for building web applications
using AI coding agents (e.g., Claude Code).

It enables:

- Safe parallel AI development
- Deterministic architecture evolution
- Strict governance and security
- Prevention of hallucinated schema/API drift
- Traceable spec promotion workflow

This framework is designed to scale from startup projects to enterprise-grade systems.

---

# 1. Philosophy

AI agents are fast implementers.
Governance ensures long-term system integrity.

We separate:

- Immutable governance layer
- Approved system design
- AI execution sandbox
- Controlled promotion workflow

No AI agent can silently modify architecture.

---

# 2. Spec Structure

/spec
  /00_global      ← Immutable governance layer
  /10_design      ← Approved architecture & contracts
  /20_backlog     ← Tasks & epics
  /30_delivery    ← Promotion log & release notes
   /40_workspace   ← AI workspace sandboxes (agent proposals and handoffs)
  /50_archive     ← Rejected proposals (optional)

---

# 3. Authority Model

Priority of truth:

1. /.spec/00_global/*
2. /.spec/10_design/*
3. /.spec/20_backlog/TASKS.md
4. Existing code
5. Agent reasoning

Agents must never override higher layers.

---

# 4. How Claude Code (or other AI agents) Operate

Each agent has:

- A defined role (backend, frontend, architect, etc.)
- A workspace folder in `/.spec/40_workspace/agent_<role>/`
- A strict execution protocol

Agents:

✔ Implement only assigned tasks  
✔ Follow global specs  
✔ Write proposals in sandbox  
✖ Do not modify global specs  
✖ Do not modify canonical design directly  

All changes to design or governance require promotion.

---

# 5. Promotion Workflow

When an agent proposes:

- API change
- Schema change
- Architectural adjustment

It must:

1. Draft proposal in:
   `/.spec/40_workspace/agent_<role>/`

2. Supervisor reviews

3. If approved:
   - Update canonical spec in /.spec/10_design/
   - Record entry in /.spec/30_delivery/PROMOTION_LOG.md

No spec change is valid without promotion log entry.

---

# 6. Example Project: Personal Blog with Admin Review Workflow

We will build:

- Public blog
- Admin dashboard
- Posts require admin approval before publication

---

## 6.1 Functional Requirements

- Users can create posts (draft state)
- Admin user reviews and approves posts
- Only approved posts are publicly visible
- Admin dashboard shows pending posts
- Role-based access control

---

## 6.2 Suggested Tech Stack (Example)

Frontend:
- React + Vite + Tailwind

Backend:
- FastAPI + Pydantic + SQLAlchemy + PostgreSQL

Auth:
- Cookie-based session or Auth0

---

# 7. Development Flow

## Step 1 — Define Backlog

Add tasks in:

/.spec/20_backlog/TASKS.md

Example:

TASK-BE-001 — Create Post model  
TASK-BE-002 — Draft/Published workflow  
TASK-BE-003 — Admin approval endpoint  
TASK-FE-001 — Public blog page  
TASK-FE-002 — Admin dashboard  
TASK-SEC-001 — Role-based authorization  

---

## Step 2 — Design constraints and specifications

In:

/.spec/10_design/

Define:

- DB_SCHEMA.md
- DOMAIN_MODEL.md
- API_CONTRACTS.md

Example Post schema:

- id
- title
- content
- status (draft | pending | approved | rejected)
- author_id
- created_at
- approved_at

---

## Step 3 — Agents Implement in Parallel

Backend agent:
- Implements endpoints per spec

Frontend agent:
- Implements UI using API contracts

Security agent:
- Verifies role enforcement

All agents write notes in:

`/.spec/40_workspace/agent_<role>/`

---

## Step 4 — Supervisor Review

Supervisor checks:

- Alignment with global specs
- Security compliance
- No schema drift
- Acceptance criteria satisfied

If architecture changed → promotion required.

---

## Step 5 — Promotion (If Needed)

If admin approval workflow required schema update:

1. Proposal written in sandbox
2. Supervisor approves
3. Log in PROMOTION_LOG.md
4. Supervisor executes

---

# 8. Role-Based Approval Flow Example

Post Lifecycle:

Draft → Pending Review → Approved → Published

Rules:

- Author can edit draft
- Author cannot publish
- Admin can approve/reject
- Only approved posts visible in public API

Security checks must be server-side.

---

# 9. Agent Responsibilities

Supervisor:
- Governance gatekeeper
- Promotion authority
- Default orchestration entrypoint for non-trivial work

Architect:
- Define contracts & domain models
- Propose structural changes

Backend:
- Implement endpoints
- Enforce authorization

Frontend:
- Implement UI states
- Respect API contracts

Security:
- Validate auth rules
- Review exposure risks

DevOps:
- Ensure release integrity
- Block deployment if governance violated

---

# 10. What This Framework Prevents

Without governance:
- AI modifies schema silently
- API contracts drift
- Security holes appear
- Spec becomes meaningless
- Parallel agents conflict

With governance:
- Architecture remains stable
- All changes traceable
- Schema safe
- Deterministic development
- Safe parallel execution

---

# 11. Running Parallel AI Safely

Rules:

- Each agent works only in its sandbox
- Shared files require Supervisor coordination
- Schema/API changes land first
- Promotions logged before release

This allows multiple AI agents to work simultaneously without chaos.

Standard agent artifacts:

- `CLARIFICATIONS.md`
- `PROPOSED_CHANGES.md`
- `HANDOFF.md`
- `SECURITY_REVIEW.md`

---

# 12. Why This Framework Works

It creates:

- Stable governance
- Controlled AI execution
- Deterministic architecture evolution
- Auditability
- Enterprise readiness

You are not just building a blog.

You are building a scalable AI-native engineering system.

---

# 13. Recommended Next Enhancements

- Automated spec drift detection
- Supervisor scoring matrix
- ADR (Architecture Decision Record) template
- CI checks validating promotion log entries
- JSON output templates for AI agents

---

# Final Principle

Speed comes from AI.
Stability comes from governance.

This framework gives you both.
