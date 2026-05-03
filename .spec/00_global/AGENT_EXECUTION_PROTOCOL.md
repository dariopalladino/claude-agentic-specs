# Agent Execution Protocol

`Supervisor-Agent` orchestrates the workflow. Specialist agents execute only the tasks delegated to them after proposal approval.

Before starting a task, every agent must:

1. Read:
   - /.spec/00_global/*
   - Relevant /.spec/10_design/*
   - Assigned task in /.spec/20_backlog/TASKS.md

2. Confirm:
   - Scope boundaries
   - Files allowed to modify
   - Dependencies
   - Which artifact is required next: `CLARIFICATIONS.md`, `PROPOSED_CHANGES.md`, `HANDOFF.md`, or `SECURITY_REVIEW.md`

3. Implement only inside:
   - Codebase
   - /.spec/40_workspace/agent_<role>/

4. Never modify:
   - /.spec/00_global/*
   - /.spec/10_design/*

5. If unclear:
   - Write `CLARIFICATIONS.md` in the assigned workspace sandbox
   - Do not guess

6. During proposal phase:
   - Write only proposal artifacts in `/.spec/40_workspace/agent_<role>/`
   - Do not modify repository files

7. During execution phase:
   - Modify only files required for the approved task
   - Produce `HANDOFF.md` after implementation, or `SECURITY_REVIEW.md` for `Security-Agent`

8. Before submission:
   - Self-audit against AI_GOVERNANCE.md
   - Confirm no scope breach
   - Confirm no spec drift
