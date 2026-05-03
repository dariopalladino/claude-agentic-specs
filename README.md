# Agentic Spec-Driven Development Framework

This repository is a governance-first template for building software with multiple AI coding agents working in parallel.

It is designed to answer a hard problem: how do you move fast with AI agents without losing architecture quality, security, and traceability?

The approach in this repo is:

- Define rules and architecture in versioned specs first.
- Let specialized agents implement within strict boundaries.
- Require explicit proposal, review, and handoff artifacts.
- Promote architectural changes through a controlled approval path.

## What This Repository Is About

At a high level, this repo provides:

- An operating model for supervisor + specialist agents.
- A canonical spec hierarchy that acts as the source of truth.
- A sandboxed workspace pattern for agent outputs.
- Governance rules that prevent schema/API drift and unsafe changes.

It is both:

- A reusable framework for team workflows.
- A concrete blueprint for a reference multi-client product architecture with shared backend contracts.


## Repository Structure

```text
.
├── AGENTS.md                 # Project-level operating rules for all agents
├── CLAUDE.md                 # Core repository instructions and entry points
├── PROMPT.md                 # Example prompt to start agent workflow
├── TODO.md                   # Priority-driven task list scaffold
├── .claude/
│   ├── agents/               # Role-specific agent definitions
│   └── skills/               # Reusable capability skills
└── .spec/
    ├── 00_global/            # Immutable governance, quality, security, protocol
    ├── 10_design/            # Canonical architecture/design contracts
    ├── 15_requirements/      # Requirements driven priorities
    ├── 20_backlog/           # Task backlog (currently empty)
    ├── 30_delivery/          # Promotion logs/release artifacts (currently empty)
    ├── 40_workspace/         # Agent sandbox artifacts (currently empty)
    └── 50_archive/           # Archived or rejected proposals
```

## Source of Truth Hierarchy

When there is any conflict, authority is resolved in this order:

1. `.spec/00_global/*`
2. `.spec/10_design/*`
3. `.spec/20_backlog/TASKS.md` (or backlog task source)
4. Existing code
5. Agent interpretation

This means agents must follow specs, not improvise architecture.

## Agent Model

The project is organized around one orchestrator plus specialist agents.

Primary roles:

- Supervisor-Agent: orchestration, review, and final authority on cross-cutting changes.
- Planner-Agent: decomposition and execution planning.
- Architect-Agent: architecture, contracts, and boundaries.
- Backend-Agent: backend implementation and tests.
- Frontend-Agent: UI implementation aligned to contracts.
- Data-Agent: schema/query/index concerns.
- Devops-Agent: deployment/build/reliability.
- Security-Agent: security review and posture checks.

## Mandatory Artifacts and Phases

Specialist agents use structured artifacts in `.spec/40_workspace/agent_<role>/`.

- Clarification phase (when needed): `CLARIFICATIONS.md`
- Proposal phase: `PROPOSED_CHANGES.md`
- Execution completion: `HANDOFF.md`
- Security outcome (security agent): `SECURITY_REVIEW.md`

Important rule: no repository file modifications during proposal phase.

## Governance and Quality Gates

Global specs define strict controls for:

- Architecture integrity
- API and schema change governance
- Security guardrails
- Deterministic execution behavior
- Parallel development coordination

A task is considered done only when Definition of Done conditions are met, including:

- Acceptance criteria satisfied
- Tests and type checks passing
- No lint issues
- Security checks passing
- Documentation updated where needed

For backend work, the baseline quality gates are:

- `pytest`
- `pytest-asyncio`
- `mypy`

## Reference Platform Blueprint in Specs

The design specs define a reference architecture for a shared backend serving both web and native clients:

- Backend APIs under `backend/` using FastAPI-first contracts
- Web client under `web/` using React + TypeScript
- Native client under `native/` using Kotlin + Jetpack Compose
- OIDC authentication with server-side JWT verification
- RBAC authorization with minimum roles `user` and `admin`
- Internal user identity mapping (`provider`, `provider_sub`) as the foundation for foreign-key ownership and audit fields

### Technical Baseline Defined in Specs

The current canonical design under `.spec/10_design/FOUNDATIONS.md` specifies:

- Backend (`backend/`)
    - Python 3.12+
    - FastAPI + Starlette + Uvicorn
    - SQLAlchemy 2 async + Alembic
    - PostgreSQL with `asyncpg`
    - Auth0 JWT verification with `python-jose` + JWKS
    - OpenAI SDK (async)
    - `pytest` + `pytest-asyncio` + `mypy`
- Native (`native/`)
    - Kotlin 1.9
    - Jetpack Compose + Material3 + Navigation Compose
    - Room + DataStore + WorkManager
    - Retrofit + OkHttp
    - Auth0 Android SDK
    - MediaRecorder / MediaPlayer
- Web (`web/`)
    - React 19 + TypeScript
    - Vite 6
    - Auth0 React SDK
    - Tailwind via CDN in `index.html`
    - Gemini SDK `@google/genai`

Related contracts:

- `.spec/10_design/API_CONTRACTS.md`: FastAPI endpoint, layering, error, pagination, testing, and client compatibility rules
- `.spec/10_design/AUTHORIZATION_MODEL.md`: OIDC + RBAC model, user table requirements, and foreign-key policy

Note: some documents in this repo describe general framework examples and may differ from the current canonical design. For implementation decisions, always follow `.spec/00_global/*` and `.spec/10_design/*` first.

## How to Work in This Repo

Typical flow:

1. Read global and design specs relevant to the task.
2. Pick or define a backlog task.
3. Produce `PROPOSED_CHANGES.md` in the role sandbox.
4. Obtain supervisor approval.
5. Implement only approved scope.
6. Produce `HANDOFF.md` (or `SECURITY_REVIEW.md`).
7. If architecture/schema/API changed, promote updates and log them.

## Local Development Notes (macOS)

Documented project commands:

- Type check: `mypy .`
- Tests: `pytest -q`
- Lint: any linting system that works on this environment

If using the documented virtual environment path:

```bash
source .venv312/bin/activate
```

To create a feature branch:

```bash
git checkout -b f-<branch-name>
```

## Using the Task List

`TODO.md` contains a priority model:

- Priority 1: must implement
- Priority 2: optional if expensive
- Priority 3: must not implement

Recommended next step is to replace placeholder TODO items with concrete task IDs and acceptance criteria aligned to `.spec/10_design/*`.

## Why This Framework Exists

This repository is optimized for teams that want:

- AI-assisted velocity
- predictable system evolution
- explicit review points for risky changes
- auditable delivery behavior

In short, it is a control plane for AI-assisted software delivery, not just a code dump.
