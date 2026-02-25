# Promotion Log EXAMPLES

## 2026-02-16: Framework Decision - Django over FastAPI
- **Decision**: Replace FastAPI with Django as the application framework
- **Rationale**: User requirement for server-side rendering to ensure SEO meta tags are in initial HTML response. Django provides SSR natively, eliminates need for separate API+SPA, simplifies deployment.
- **Files Updated**:
  - /.spec/10_design/FOUNDATIONS.md - Complete rewrite for Django stack
  - /.spec/10_design/API_CONTRACTS.md - Updated for Django views pattern
  - /.spec/10_design/INTEGRATIONS.md - Added Auth0 OIDC integration details
- **Impact**: All agents must use Django (not FastAPI). SQLAlchemy remains for data access. Pydantic remains for validation.