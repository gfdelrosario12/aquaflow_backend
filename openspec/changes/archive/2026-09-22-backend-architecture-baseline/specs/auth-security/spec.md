## Purpose

Provide JWT-based authentication and role-based authorization for secure API access to the irrigation system.

## ADDED Requirements

### Requirement: System SHALL authenticate users via JWT
The system SHALL issue JWT tokens upon successful login and validate tokens on each request.

#### Scenario: Successful authentication
- **WHEN** user submits valid credentials
- **THEN** system returns JWT access token and refresh token

### Requirement: System SHALL enforce role-based access control
API endpoints SHALL be protected by role-based authorization (ADMIN, OPERATOR, VIEWER roles).

#### Scenario: Restricted admin access
- **WHEN** viewer-role user attempts to access admin endpoint
- **THEN** system rejects request with 403 Forbidden

### Requirement: System SHALL support token refresh
The system SHALL allow token renewal using a refresh token before access token expiry.

#### Scenario: Refresh access token
- **WHEN** client submits refresh token
- **THEN** system returns new access token if refresh token is valid

### Requirement: System SHALL password-protect user accounts
User passwords SHALL be hashed with BCrypt before storage.

#### Scenario: Store user password
- **WHEN** user creates or updates password
- **THEN** system stores only BCrypt-hashed version
