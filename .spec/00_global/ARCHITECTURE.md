# Architecture Guidelines

## Layering

Typical layers:

- Presentation (UI / API Routes)
- Application / Service Layer
- Domain Logic
- Data Access Layer
- Infrastructure (external services)

Business rules must not live in:
- UI components
- Controllers/routes
- Migration scripts

## API-First Approach

- All backend systems expose clearly defined APIs: /.spec/10_design/AI_CONTRACTS.md
- Contracts must be documented.
- Responses must be consistent and typed.

## Modularity

Modules should:
- Have a clear responsibility
- Avoid circular dependencies
- Avoid cross-module direct DB access

## Configuration

- Environment-based configuration
- No hardcoded secrets
- Clear separation of dev/staging/prod

## Extensibility

New features must:
- Plug into existing patterns
- Not introduce parallel architecture styles
- Reuse shared utilities
