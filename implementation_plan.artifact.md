# Homever Backend & Android Integration Plan

This plan describes the steps to create a new NestJS backend with PostgreSQL, integrate it with Cognito, and update the Android app to connect correctly to the new backend.

## 1. Backend Architecture (NestJS + Prisma + PostgreSQL)
- **Directory**: `backend/`
- **Stack**: Node.js, NestJS (TypeScript), Prisma ORM, PostgreSQL.
- **Database Schema (`prisma/schema.prisma`)**:
  - `Profile` model: `id` (UUID), `cognitoSub` (String, Unique), `displayName` (String), `gender` (String), `age` (Int), `occupation` (String), `avatarUrl` (String, Optional).
- **Authentication**: `JwtAuthGuard` will use `aws-jwt-verify` to validate Cognito Access Tokens. Extract `sub` as the identity.
- **Endpoints**:
  - `GET /v1/users/me`: Return 200 with profile, or 404 `{ "code": "PROFILE_NOT_FOUND" }`.
  - `POST /v1/users/me/avatar/presigned-url`: Generate a short-lived S3 pre-signed PUT URL using `aws-sdk/client-s3`.
  - `POST /v1/users/me`: Create the profile (mapping `displayName`, `gender`, `age`, `occupation`, `avatarUrl`) using `cognitoSub`. Returns 201.

## 2. Android App Changes
- **API Base URL**: Change Retrofit `baseUrl` in `MainActivity.kt` to `http://10.0.2.2:3000/v1/`.
- **API Interface**:
  - Update `HomeverApi.kt`: Change `@PUT("users/me")` to `@POST("users/me")` to match the requirement.
- **Profile Check Flow**:
  - Update `ProfileRepositoryImpl.kt` and `ProfileViewModel.kt` to properly detect HTTP 404 and the `"PROFILE_NOT_FOUND"` error code. If detected, emit `onResult(false)` so `MainActivity` navigates to `"profile"`.
- **Auth Scope**: `CognitoAuthManager.kt` was previously updated to request only `openid`, which fixes the `invalid_scope` error. The Access Token (not ID token) will be used as the Bearer token in Retrofit (handled by `AuthInterceptor`).

## 3. Local Development & Docker
- Create a `docker-compose.yml` for PostgreSQL in the `backend/` folder.
- Provide a `README.md` and `.env.example` in `backend/` explaining how to start the DB, run Prisma migrations, and start the NestJS API.

## 4. Verification Plan
- **Automated Tests**: I'll add e2e tests for NestJS verifying the token parsing and endpoints.
- **Android Integration Test**: I'll build and run the Android app, testing the login flow, missing profile detection, S3 presigned URL retrieval, and profile creation.

Once approved, I'll generate the backend code and apply the necessary Android modifications.