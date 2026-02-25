# Agent Operating Rules (Project)

## Goal
Work as a supervisor + subagents. Supervisor delegates tasks, reviews evidence, and finalizes output.
Agents must not modify repository files during PROPOSED_CHANGES phase. Only execution phase allows file modifications.

## Available Agents
- ARCHITECT
- BACKEND
- DATA
- FRONTEND
- DEVOPS
- PLANNER
- SECURITY_QA


## Definition of Done (DoD)
Only for Supervisor Agent
Read the .spec/00_global/DEFINITION_OF_DONE.md in its entirety to conceptualize the definition of done.


## Commands
- For installation commands remember we are developing in a MacOS system
- Lint: any linting system that works for you on this system
- Typecheck: mypy .
- Tests: pytest -q


## Conventions
- Prefer small PR-sized diffs
- Add/adjust tests for behavior changes
- Don’t commit secrets
- Document any new env vars in README


## Proposed Change format (MANDATORY)
For all agents: ARCHITECT, PLANNER, BACKEND, FRONTEND, DEVOPS, SECURITY, DATA.
Return a PROPOSED_CHANGE.md directive in .spec/40_workspace/agent_<agent name>/ when the work is clear and ready to start, and use the following template:

### PROPOSED_CHANGE
- Task: <task reference>
- Intent: What problem this solves.
- Files to modify: list
- Implementation plan: Step-by-step changes.
- Risks: Potential regressions or edge cases.
- Test plan: Commands to run and expected results.


## Clarifications format (ONLY WHEN REQUIRED)
Used when:
- requirements are ambiguous architecture decisions are missing spec conflicts exist assumptions would be risky

Not used for:
- small implementation choices stylistic decisions things covered in specs

Rule:
- If the agent can safely choose, it should choose.
- If the choice affects architecture, contracts, or behavior — ask.

### CLARIFICATIONS
- Task: <task reference or description>
- Blocking Questions: 
#### Q1: <short question title>
- Context:
- Explain what is unclear.
- Options:
    - Option A: ...
    - Option B: ...
    - Option C: ...
- Impact: What changes depending on the decision.
- Recommendation: Agent’s preferred option and why.

#### Q2: ...


## Handoff format (MANDATORY)
Return an HANDOFF.md directive in .spec/40_workspace/agent_<agent name>/ when the work is done and use the following template:

### HANDOFF
- Status: done | blocked | needs_review
- Summary: 3–6 bullets
- Files changed: list
- Commands run: list
- Evidence: paste key outputs (short)
- Risks/edge cases: bullets
- Next steps: bullets


## Governance
Only for Supervisor Agent:
Read these directive file in its entirety from .spec/00_global/GOVERNANCE.md


## AI Governance
Only for Supervisor Agent:
Read these directive file in its entirety from .spec/00_global/AI_GOVERNANCE.md


## Agent Execution Protocol
Read these directive file in its entirety from .spec/00_global/AGENT_EXECUTION_PROTOCOL.md


## Architecture
Only for Architect Agent:
Read these directive file in its entirety from .spec/00_global/ARCHITECTURE.md


## API Convention
Only for Backend Agent:
Read these directive file in its entirety from .spec/00_global/API_CONVENTIONS.md


## Non-Functional Requirements
Only for Planner Agent:
Read these directive file in its entirety from .spec/00_global/NONFUNCTIONAL.md


## Principles
For all Agents:
Read these directive file in its entirety from .spec/00_global/PRINCIPLES.md


## Security
Only for Security Agent:
Read these directive file in its entirety from .spec/00_global/SECURITY.md


## UI Conventions
Only for Frontend Agent:
Read these directive file in its entirety from .spec/00_global/UI_CONVENTIONS.md


## Vision
Only for Supervisor Agent:
Read these directive file in its entirety from .spec/00_global/VISION.md