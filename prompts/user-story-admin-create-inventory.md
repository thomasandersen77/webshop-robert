# User Story: Admin creates Inventory for a Product in a Category

## Goal

Allow an admin to create inventory for a product in a category, with the rule that the `ProductCategory` must already exist so the product is linked correctly.

## User Story

As an **Admin**,  
I want to **create Inventory for a Product in an existing ProductCategory**,  
so that **catalog structure and stock data stay consistent and linked correctly**.

## Business Context

Inventory creation depends on valid catalog structure.

A category must already exist before the product can be linked correctly.

Depending on the domain design, inventory creation may either:
- create a new product and its first inventory record in one workflow
- or create inventory for an already existing product

This must be clarified explicitly.

## Scope

This use case covers:

- validating that the referenced category exists
- creating or linking the product according to the chosen domain flow
- creating the inventory record
- persisting the result
- returning the created inventory result

This use case does **not** cover:

- stock reservations
- checkout
- stock deduction during sales
- inventory reconciliation flows
- inventory transfer between locations

## Preconditions

- the caller is authenticated
- the caller has `ADMIN` privileges
- the referenced `ProductCategory` exists
- the request contains valid data for the intended flow

## Main Flow

1. Admin sends a request to create inventory
2. The system validates syntactic input
3. The system verifies that the caller is authorized as `ADMIN`
4. The system verifies that the referenced `ProductCategory` exists
5. The system creates or resolves the target `Product` according to domain rules
6. The system creates the inventory record
7. The system persists the result transactionally
8. The system returns the created result

## Acceptance Criteria

### AC1: Admin can create inventory when category exists

**Given** an authenticated admin  
**And** the referenced `ProductCategory` exists  
**And** the request data is valid  
**When** the admin creates inventory  
**Then** the system persists the inventory  
**And** the inventory is linked correctly to the product and category  
**And** the created result is returned

### AC2: Inventory creation fails when category does not exist

**Given** an authenticated admin  
**And** the referenced `ProductCategory` does not exist  
**When** the admin attempts to create inventory  
**Then** the request is rejected with a business error

### AC3: Non-admin cannot create inventory

**Given** an authenticated non-admin user  
**When** the user attempts to create inventory  
**Then** the request is rejected with an authorization error

### AC4: Required quantity/stock data must be valid

**Given** an authenticated admin  
**When** the admin attempts to create inventory with invalid stock data  
**Then** the request is rejected

### AC5: Product-category linkage must be consistent

**Given** an authenticated admin  
**When** inventory is created  
**Then** the product must be linked to a valid existing category  
**And** the system must not create an unlinked or orphaned inventory record

## Suggested Domain Rules

- inventory creation is an admin-only action
- category must exist before valid linkage can happen
- inventory must belong to a valid product
- product/category linkage must be explicit and valid
- state-changing operations must be transactional
- aggregate root boundaries must remain clear

## Important Design Clarification

There are two possible domain interpretations:

### Option A: Create Product and Inventory in one admin flow

In this interpretation:
- request contains product data and inventory data
- category must already exist
- system creates product first
- system creates inventory linked to that product

### Option B: Create Inventory for an existing Product

In this interpretation:
- product already exists
- request references existing product id
- category is validated through the product relationship or explicitly supplied

This must be chosen explicitly before implementation.

## Suggested API Shape

If using **Option A**:
- `POST /api/admin/inventory`

If using **Option B**:
- `POST /api/admin/products/{productId}/inventory`

## Suggested Request Shape (Option A)

```json
{
  "categoryId": "existing-category-id",
  "name": "11kg Propane Cylinder",
  "description": "Steel propane cylinder for domestic use",
  "priceMinor": 29900,
  "ratingStars": 5,
  "quantity": 100
}
```

## Suggested Response Shape

```json
{
  "inventoryId": "generated-id",
  "productId": "generated-or-existing-product-id",
  "categoryId": "existing-category-id",
  "quantity": 100
}
```

## Domain Notes

- aggregate root boundaries must be explicit
- avoid competing write paths for product creation
- if `Product` is the aggregate root, product creation should happen through `ProductRepository`
- if `Inventory` is its own aggregate root, inventory creation must still respect existing product/category consistency
- DTOs belong only in controllers
- `core` must only use domain types
- persistence entities must be separate from domain model

## Technical Notes for AI Implementation

- controller must be thin
- request DTO maps to domain command
- service verifies admin access via `RbacService`
- service verifies category existence before linkage
- service creates or resolves product according to chosen domain design
- service creates inventory transactionally
- persistence maps domain ↔ entity explicitly
- JPA entities must be named with `Entity` suffix, e.g. `InventoryEntity`, `ProductEntity`, `ProductCategoryEntity`

## Out of Scope / Must Verify

- whether inventory is its own aggregate root or part of product aggregate
- whether inventory creation also creates product, or only links to existing product
- whether stock can be zero at creation time
- whether negative stock is forbidden absolutely
- whether multiple inventory records per product are allowed
- whether location/warehouse is part of the inventory identity
