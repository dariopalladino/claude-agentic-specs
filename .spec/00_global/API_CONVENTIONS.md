# API Conventions

## General

- Use RESTful patterns unless otherwise justified.
- Use nouns, not verbs in routes.
- Version APIs if breaking changes occur.

Example:
GET    /api/users
POST   /api/users
GET    /api/users/{id}

## Responses

Standard response structure:

{
  "data": ...,
  "meta": ...,
  "error": null
}

On error:

{
  "data": null,
  "meta": null,
  "error": {
      "code": "ERROR_CODE",
      "message": "Human readable message"
  }
}

## Validation

- All inputs validated at boundary.
- Explicit schema definitions required.
- Reject unknown or malformed fields.

## Pagination

- Use cursor or page-based consistently.
- Return pagination metadata.

## Idempotency

- PUT and DELETE must be idempotent.
- POST must document side effects.

## Authorization

- Authorization must be explicit in each endpoint.
- Never rely only on frontend enforcement.
