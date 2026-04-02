# Cursor prompt: use case 3 — create a user account using existing user table, add CustomerController, customer owns basket, store in db

You are implementing customer account creation and basket ownership.

You must follow `BACKEND_RULES.md` strictly:
- `api` is HTTP boundary only
- `core` owns all use-case services
- DTOs only exist in controllers/API
- mandatory mapping between layers:
  - writes: `DTO -> Domain -> Entity`
  - reads: `Entity -> Domain -> DTO`
- `core` uses only domain types
- JPA entities must be suffixed with `Entity`
- aggregate root discipline is critical
- no repository calls from controllers
- authorization/business rules in services
- persistence mapping explicit
- use existing user table if present
- each customer has its own basket
- store everything in database

## Goal

Implement a customer account flow where:

1. a customer account can be created using the **existing user table**
2. a new `CustomerController` is added in `api`
3. request flow is `controller -> service -> repository`
4. each customer has its own basket
5. the basket relationship is persisted in the database

---

## Critical design rules

### Reuse existing user table

You must reuse the existing user table/entity if it already exists.

Do **not** create a parallel customer-auth table if the repo already has a valid user model.

Instead:
- inspect existing `UserEntity`, `User`, roles, and current identity model
- extend or reuse the current model conservatively

### Aggregate root

Treat `User` / `Customer` ownership carefully.

Preferred direction:
- `User` remains the identity aggregate root
- customer-specific basket ownership is represented through a stable relation
- basket itself remains its own aggregate root
- basket is linked to exactly one customer user

Do not make basket ownership implicit or hidden in controller logic.

### Controller

Add a `CustomerController` in `api`.

Controller responsibilities only:
- routing
- request validation
- `@CurrentUser` when relevant
- DTO mapping
- HTTP response shaping

No business logic in controller.

### Service

Create or reuse a `core` service for:
- creating a customer account
- creating/fetching the customer basket if required by the chosen flow

Do not place use-case services in `api`.

---

## What to inspect first

Inspect the repo and verify:

1. whether `UserEntity` already exists
2. whether `User` domain type already exists
3. whether roles already include `CUSTOMER`
4. whether registration/auth endpoints already exist
5. whether there is already a user repository
6. whether basket/cart already exists or was introduced in another use case
7. whether a customer/profile concept already exists separately from user
8. how `@CurrentUser` is currently wired

Reuse existing code where valid.
Create new code only where missing.

Mark uncertainties as `MÅ VERIFISERES`.

---

## Functional requirements

### Create customer account

Support customer account creation using the existing user table.

Possible route if no better convention already exists:
- `POST /api/customers`
or
- `POST /api/auth/register`

If a registration endpoint already exists, do not duplicate behavior unnecessarily.
Instead:
- extend or adapt current design
- add `CustomerController` only where it makes sense architecturally

### Customer owns basket

Each customer must have its own basket.

Choose the correct design based on existing repo structure:

Preferred:
- one active basket per customer

Must verify:
- whether multiple baskets per customer are allowed
- whether basket is created immediately on account creation or lazily on first use

Do not guess silently.

### Persistence

The customer-to-basket relation must be stored in DB.

If schema changes are needed:
- add new Liquibase changeset
- never edit old changesets

---

## Required design direction

### Domain

Use existing user domain if present.

Possible domain concepts:
- `User`
- `Customer`
- `Role`
- `Basket` / `Cart`

Keep rich domain model.
Avoid primitive obsession.

### Persistence

Use existing `UserEntity` table/entity.

Possible additions:
- basket foreign key or ownership relation
- customer role assignment
- any required uniqueness constraints

All JPA entities must end in `Entity`.

### Security

If authenticated endpoints are involved:
- use `@CurrentUser user: User`
- pass domain user explicitly into service
- do not use singleton/global current-user access

---

## Deliverables

Respond in exactly this structure:

# A. Confirmed current state
List what already exists in the repo.

# B. MÅ VERIFISERES
List only uncertainties.

# C. Design decision
Explain how existing user table is reused, how `CustomerController` fits, and how basket ownership is modeled.

# D. Files to create or change
Table with:
- path
- action
- reason

# E. Full code
Show complete files or complete patches for:
- `CustomerController`
- request/response DTOs
- service in `core`
- repository interface(s)
- repository adapter(s)
- user/basket mapping
- Liquibase changeset if needed

# F. Endpoint contract
Show example requests and responses.

# G. Tests
Add:
- `api` tests for customer HTTP contract only
- `core` unit tests for customer/basket ownership behavior
- `core` integration tests for user table reuse + basket persistence

---

## Quality constraints

- reuse existing user table
- do not duplicate identity model unnecessarily
- controller -> service -> repository only
- no DTOs in core
- no entities in core
- basket ownership persisted in DB
- explicit domain/entity/dto mapping
- aggregate root discipline preserved
- reuse existing code first, create new only if missing
