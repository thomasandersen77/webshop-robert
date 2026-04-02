# Cursor prompt: use case 2 — create a shopping basket, add product, remove product, get total amount

You are implementing shopping basket functionality.

You must follow `BACKEND_RULES.md` strictly:
- `api` is HTTP boundary only
- `core` owns business logic
- DTOs only exist in API/controller boundary
- mapping is mandatory: `DTO -> Domain -> Entity` for writes and `Entity -> Domain -> DTO` for reads
- `core` must only use domain types
- repositories return domain objects
- all JPA entities must end with `Entity`
- aggregate root is extremely important
- no business logic in controllers
- no repository calls from controllers
- transactions on all state-changing service methods
- money must use minor units (`Long`)
- exception messages and business logs should be in Norwegian

## Goal

Implement a basket/cart flow with these capabilities:

1. create a shopping basket
2. add product to basket
3. remove product from basket
4. get basket total amount

The basket must be persisted in the database.

---

## Aggregate root direction

Treat `Basket` / `ShoppingBasket` / `Cart` as the **aggregate root**.

Choose the existing project naming convention if one already exists.
If no such concept exists, prefer **`Basket`** only if repo naming supports that; otherwise prefer **`Cart`** if that aligns better with current code.

Rules:
- all writes go through the basket aggregate root
- basket lines/items are modified only through basket behavior
- total amount is calculated from basket aggregate/domain types
- no competing write paths around basket items

---

## Required behavior

### Create basket

Support creation of a basket for a customer or session, depending on current domain direction.

If the repo already ties baskets to authenticated users, follow that.

If the repo does not yet support users/baskets properly, create the minimal correct design and document assumptions.

### Add product

Allow adding a product to a basket.

Rules:
- product must exist
- quantity must be valid and positive
- basket line behavior must be explicit
- if the product already exists in basket, choose behavior based on existing domain direction:
  - increase quantity, or
  - reject duplicate line

Do not guess silently. Document the chosen rule.

### Remove product

Allow removing product from basket.

Rules:
- removing a non-existing product must be handled explicitly
- quantity decrement vs full line removal must be chosen explicitly based on request/requirements
- if only full line removal is supported, state that clearly

### Total amount

Return total amount based on domain pricing rules.

Rules:
- total amount belongs in `core`
- controllers must never calculate totals
- money must use `Long` minor units internally
- API should return an explicit money DTO or equivalent response structure

---

## What to inspect first

Inspect the repo and verify:

1. whether `Cart`, `Basket`, `OrderDraft`, or similar concept already exists
2. whether `Product` repository already exists
3. whether money/value object already exists
4. whether basket persistence tables/entities already exist
5. whether user linkage already exists
6. whether there are any existing add/remove cart endpoints
7. whether pricing is already modeled in domain

Reuse existing code if valid.
Create new code only where missing.

Mark uncertainties as `MÅ VERIFISERES`.

---

## Required design

### API

Implement thin controllers only.

Possible endpoints if no better convention exists:

- `POST /api/baskets`
- `POST /api/baskets/{basketId}/items`
- `DELETE /api/baskets/{basketId}/items/{productId}`
- `GET /api/baskets/{basketId}`

If authenticated customer ownership is already modeled, prefer routes under:
- `/api/cart`
or another existing convention

Use current project conventions if already established.

### Core

Implement:
- basket aggregate root
- basket item/line as child entity/value type
- application service in `core`
- repository interface in `core`
- domain methods for:
  - create basket
  - add product
  - remove product
  - calculate total

### Persistence

Implement if missing:
- `BasketEntity`
- `BasketItemEntity`
- repository adapter
- explicit domain/entity mapping

---

## Persistence and DB rules

If new schema is needed:
- add a new Liquibase changeset
- never edit old changesets
- keep schema aligned with aggregate design

Possible persisted concepts:
- basket
- basket item
- customer-to-basket relation if already modeled
- timestamps/status if current design requires them

Do not create unnecessary tables if a simpler aggregate design works.

---

## Deliverables

Respond in exactly this structure:

# A. Confirmed current state
List what already exists in the repo.

# B. MÅ VERIFISERES
List only uncertainties.

# C. Aggregate root design
Explain briefly why the basket/cart is the aggregate root and how add/remove/total go through it.

# D. Files to create or change
Table with:
- path
- action
- reason

# E. Full code
Show complete files or complete patches for:
- controller(s)
- request/response DTOs
- basket aggregate/domain types
- service
- repository interface
- repository adapter
- entity/domain mappers
- Liquibase changeset if needed

# F. Endpoint contract
Show example requests and responses.

# G. Tests
Add:
- `api` tests for HTTP contract only
- `core` unit tests for basket behavior
- `core` integration tests for persistence + totals + add/remove flow

---

## Quality constraints

- no business logic in controller
- no DTOs in core
- no entities in core
- total amount calculated only in domain/core
- money in minor units
- aggregate root discipline enforced
- persist in database
- reuse existing code first
