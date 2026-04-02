# User Story: Admin creates a Product

## Goal

Allow an admin to create a `Product` with `price`, `name`, `description`, and `rating (stars)` so that it can exist as a valid catalog item.

## User Story

As an **Admin**,  
I want to **create a Product with price, name, description, and rating**,  
so that **the catalog contains complete product information for later inventory and sales flows**.

## Business Context

A `Product` is a domain concept in the catalog.

A product should belong to a valid `ProductCategory`.

This use case is about product creation only. Inventory creation is handled separately.

## Scope

This use case covers:

- creating a new product
- assigning it to an existing `ProductCategory`
- validating product data
- persisting the product
- returning the created product

This use case does **not** cover:

- creating inventory entries
- stock movement
- updating products
- deleting products
- review system logic for customer-submitted ratings

## Preconditions

- the caller is authenticated
- the caller has `ADMIN` privileges
- the target `ProductCategory` already exists
- the request contains valid product data

## Main Flow

1. Admin sends a request to create a `Product`
2. The system validates syntactic input
3. The system verifies that the caller is authorized as `ADMIN`
4. The system verifies that the referenced `ProductCategory` exists
5. The system validates product domain rules
6. The system persists the product
7. The system returns the created product

## Acceptance Criteria

### AC1: Admin can create product with valid data

**Given** an authenticated admin  
**And** the referenced `ProductCategory` exists  
**And** the product data is valid  
**When** the admin creates a `Product`  
**Then** the product is persisted  
**And** the created product is returned

### AC2: Product must reference an existing category

**Given** an authenticated admin  
**And** the referenced category does not exist  
**When** the admin attempts to create a `Product`  
**Then** the request is rejected with a business error

### AC3: Non-admin cannot create product

**Given** an authenticated non-admin user  
**When** the user attempts to create a `Product`  
**Then** the request is rejected with an authorization error

### AC4: Name is required

**Given** an authenticated admin  
**When** the admin attempts to create a product without a name  
**Then** the request is rejected

### AC5: Price must be valid

**Given** an authenticated admin  
**When** the admin attempts to create a product with invalid price  
**Then** the request is rejected

### AC6: Rating must be valid

**Given** an authenticated admin  
**When** the admin attempts to create a product with invalid `rating (stars)`  
**Then** the request is rejected

## Suggested Domain Rules

- `Product` is an aggregate root
- `Product` must have identity
- `name` must not be blank
- `description` must follow domain length/format rules
- `price` must be represented in minor units in domain logic
- `rating (stars)` must be validated according to domain constraints
- referenced `ProductCategory` must exist before product creation
- product creation is an admin-only action

## Suggested Interpretation of Rating

Because `rating (stars)` can mean different things, one interpretation must be chosen explicitly.

Suggested conservative rule:
- rating is an integer number of stars
- allowed range is `1..5`

This must be verified if the domain intends:
- `0..5`
- decimals like `4.5`
- derived rating only, not admin-managed input

## Suggested API Shape

- `POST /api/admin/products`

## Suggested Request Shape

```json
{
  "categoryId": "existing-category-id",
  "name": "11kg Propane Cylinder",
  "description": "Steel propane cylinder for domestic use",
  "priceMinor": 29900,
  "ratingStars": 5
}
```

## Suggested Response Shape

```json
{
  "id": "generated-id",
  "categoryId": "existing-category-id",
  "name": "11kg Propane Cylinder",
  "description": "Steel propane cylinder for domestic use",
  "priceMinor": 29900,
  "ratingStars": 5
}
```

## Domain Notes

- `Product` should be modeled as an aggregate root
- category reference should be modeled explicitly in domain
- DTOs belong only in `api`
- `core` must only use domain types
- JPA entities must be separate from domain model
- persistence mapping must be explicit

## Technical Notes for AI Implementation

- controller must be thin
- request DTO maps to domain command
- service verifies admin access via `RbacService`
- service verifies category existence
- service creates the `Product` aggregate
- repository persists the product aggregate
- persistence maps domain ↔ entity explicitly
- JPA entity must be named `ProductEntity`

## Out of Scope / Must Verify

- whether rating is admin-managed or derived from reviews
- whether a product can be created in draft/unpublished state
- whether SKU is required
- whether price currency is always NOK
- whether duplicate product names are allowed inside the same category
