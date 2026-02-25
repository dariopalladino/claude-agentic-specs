# Agent Execution Protocol

Before starting a task, every agent must:

1. Read:
   - /.spec/00_global/*
   - Relevant /.spec/10_design/*
   - Assigned task in /.spec/20_backlog/TASKS.md

2. Confirm:
   - Scope boundaries
   - Files allowed to modify
   - Dependencies

3. Implement only inside:
   - Codebase
   - /.spec/40_agents/<agent_name>/

4. Never modify:
   - /.spec/00_global/*
   - /.spec/10_design/*

5. If unclear:
   - Write clarification request in sandbox
   - Do not guess

6. Before submission:
   - Self-audit against AI_GOVERNANCE.md
   - Confirm no scope breach
   - Confirm no spec drift
