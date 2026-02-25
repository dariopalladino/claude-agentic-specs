### PROPOSED_CHANGE EXAMPLE
- Task: TASK-005 follow-up fix for initial Alembic migration enum creation
- Intent: Prevent duplicate PostgreSQL ENUM creation (`user_role`, `article_status`) during `alembic upgrade` on fresh or partially initialized databases.
- Files to modify: app/alembic/versions/001_initial_schema.py
- Implementation plan: 1) Replace generic `sa.Enum` enum declarations with PostgreSQL `ENUM` objects. 2) Explicitly create enums once with `checkfirst=True`. 3) Reuse those enum objects in table columns so Alembic does not auto-emit duplicate `CREATE TYPE` statements.
- Risks: Low risk; migration SQL generation changes for enum handling. Existing DBs already migrated are unaffected, but downgrade behavior should still be verified.
- Test plan: 1) `cd app && alembic -c alembic/alembic.ini upgrade head` on a fresh DB should succeed without DuplicateObject. 2) `cd app && alembic -c alembic/alembic.ini downgrade base` should drop tables and enums cleanly.
