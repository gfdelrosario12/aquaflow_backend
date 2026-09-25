# OpenSpec Proposal: openapi-documentation

## Goal
Add comprehensive OpenAPI 3.0 documentation for the AquaFlow `/api/v1` backend using `springdoc-openapi` (v2.8.5). Document all REST endpoints, DTO request/response schemas, security requirements (`bearerAuth`), authorization roles, error payloads, lifecycle states, and correlation IDs without exposing credentials or internal persistence entities.

## Objectives
1. Configure `OpenApiConfig` with OpenAPI 3.0 metadata, contact details, license, and `bearerAuth` security scheme.
2. Annotate API controllers with detailed OpenAPI operation descriptions, responses, parameter schemas, and error responses.
3. Annotate DTO request and response objects with `@Schema` metadata for accurate OpenAPI schema generation.
4. Add `OpenApiIntegrationTest` to verify that `/v3/api-docs` returns a valid OpenAPI spec containing title, security schemes, and API paths.
5. Ensure 100% test suite pass rate.
