# Cursor prompt: use case 1 — get all product categories and products

You are implementing **read functionality** for:
- getting all `ProductCategory`
- getting products grouped by category
- `ProductCategory` is the **aggregate root**

You must follow `BACKEND_RULES.md` strictly:
- `api` is HTTP boundary only
- `core` owns business logic and use-case services
- DTOs only exist in controllers / API boundary
- mapping is mandatory between layers: `Entity -> Domain -> DTO` for reads
- `core` must use only domain types
- repositories return domain objects, never DTOs
- all JPA entities must be suffixed with `Entity`
- aggregate root discipline is critical
- controller must be ultra-thin
- service/use-case logic belongs in `core`
- persistence mapping must be explicit
- no JPA entities in `core`
- no repository calls from controllers

## Goal

Implement a use case where the frontend can retrieve:

1. all product categories
2. each category with its products
3. optionally an endpoint for one category with its products if the existing structure suggests it

`ProductCategory` must be the aggregate root for this read model.

---

## Required design direction

### Aggregate root

Treat `ProductCategory` as the aggregate root.

That means:
- category repository is the main entry point for this use case
- product traversal happens through category boundaries
- do not create competing read paths that bypass category ownership unless existing code clearly requires it

### Layering

Use this structure:

- `api`
  - controller
  - request/response DTOs
  - DTO mapping only
- `core`
  - `ProductCategory` aggregate root
  - child product domain types or references
  - query service / application service
  - repository interface
- persistence/infrastructure
  - `ProductCategoryEntity`
  - `ProductEntity`
  - repository adapter
  - entity-to-domain mapping

### Mapping

For this use case, mapping must be explicit:

- `ProductCategoryEntity -> ProductCategory`
- `ProductEntity -> Product`
- `ProductCategory -> ProductCategoryResponseDto`
- `Product -> ProductResponseDto`

Do not return entities directly from controller.
Do not return DTOs from repository.

---

## What to inspect first

Inspect the repo and verify:

1. whether `ProductCategory` already exists
2. whether `Product` already exists
3. whether category/product relation already exists in domain and/or persistence
4. whether a repository for category already exists
5. whether a query service already exists
6. whether there are existing admin/customer product/category endpoints
7. whether current code already violates aggregate root ownership and needs conservative correction

Reuse existing code where valid.
Create new code only where missing.

Do not guess. Mark uncertain parts as `MÅ VERIFISERES`.

---

## Use case requirements

### Functional requirements

Support one or both of these endpoint shapes if the repo structure supports them:

- `GET /api/product-categories`
- `GET /api/product-categories/{categoryId}`

Preferred behavior:

- `GET /api/product-categories` returns categories with their products for browse/homepage use
- `GET /api/product-categories/{categoryId}` returns one category with its products

If the repo already has a better established route naming convention, follow that instead.

### Response requirements

The response should be frontend-friendly and explicit.

Each category should include:
- id
- name
- products

Each product should include at minimum if present in domain:
- id
- name
- description
- price
- rating
- image information if already supported

Do not invent fields that are not supported by current domain/persistence.

---

## Business rules

- only published/visible products should be returned **if** the domain already supports publication state
- category without products should still be returned **if** requirements allow it
- product/category linkage must be represented consistently through the category aggregate root
- read operations should be `@Transactional(readOnly = true)` where appropriate

Anything not confirmable from code must be listed under `MÅ VERIFISERES`.

---

## Deliverables

Respond in exactly this structure:

# A. Confirmed current state
List what already exists in the repo.

# B. MÅ VERIFISERES
List only uncertainties.

# C. Design decision
Explain briefly why `ProductCategory` is the aggregate root and how read flow follows that.

# D. Files to create or change
Table with:
- path
- action
- reason

# E. Full code
Show complete files or complete patches for:
- controller
- response DTOs
- service/query service
- repository interface
- repository adapter
- entity/domain mappers

# F. Endpoint contract
Show example JSON response(s).

# G. Tests
Add:
- `api` tests for HTTP contract only
- `core` integration tests for category + product retrieval through service and repository adapter

---

## Quality constraints

- no controller business logic
- no DTOs in `core`
- no entities in `core`
- category repository must return domain objects
- explicit mapping between entity/domain/dto
- aggregate root discipline must be preserved
- reuse existing code first
