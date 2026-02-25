# Non-Functional Requirements

## Performance
- No N+1 queries.
- Avoid blocking calls in async systems.
- Pagination required for large lists.
- API response time target: < 300ms for standard operations.

## Scalability
- Stateless services preferred.
- Horizontal scaling compatible.
- External dependencies abstracted.

## Reliability
- Graceful error handling.
- Retries where appropriate.
- No silent failures.

## Observability
- Structured logging.
- Errors are logged with context.
- Important business actions are auditable.

## Maintainability
- Clear naming conventions.
- Avoid premature optimization.
- Avoid deeply nested logic.

## Documentation
- Public APIs documented.
- Complex logic commented.
- Architecture decisions recorded when relevant.
