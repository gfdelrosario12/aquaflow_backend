# OpenSpec Design: openapi-documentation

## Architectural Decisions
1. **SpringDoc OpenAPI 2.8.5 Integration**: Upgraded `springdoc-openapi-starter-webmvc-ui` to `2.8.5` to maintain compatibility with Spring Framework 6.2 / Spring Boot 3.4.
2. **Global Security Scheme**: Added `bearerAuth` HTTP Bearer scheme in `OpenApiConfig` bean definition for JWT authentication documentation.
3. **Controller Annotations**: Configured `@Tag`, `@Operation`, `@ApiResponse`, and `@Parameter` across all REST controllers in `com.aquaflow.backend.api`.
4. **DTO Schema Annotations**: Enhanced `@Schema` annotations across request/response DTOs to specify field descriptions, example values, and nullability constraints.
5. **Security & Data Privacy**: Ensured credentials, raw tokens, AppKeys, and internal persistence entities are omitted from API response schemas.
