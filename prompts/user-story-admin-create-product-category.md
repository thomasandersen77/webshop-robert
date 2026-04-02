# User Story: Admin creates a ProductCategory

## Goal

Allow an admin to create a `ProductCategory` so that products can later be organized under a valid category.

## User Story

As an **Admin**,  
I want to **create a ProductCategory**,  
so that **products can be grouped and managed consistently in the catalog**.

## Business Context

`ProductCategory` is a domain concept used to classify products.

A category must exist before inventory can be created for a product that belongs to that category.

## Scope

This use case covers:

- creating a new product category
- validating that the category input is valid
- ensuring duplicate categories are handled according to domain rules
- returning the created category

This use case does **not** cover:

- updating an existing category
- deleting a category
- bulk import of categories

## Preconditions

- the caller is authenticated
- the caller has `ADMIN` privileges
- the request contains the required category data

## Main Flow

1. Admin sends a request to create a new `ProductCategory`
2. The system validates syntactic input
3. The system verifies that the caller is authorized as `ADMIN`
4. The system validates domain rules for category creation
5. The system persists the new category
6. The system returns the created category

## Acceptance Criteria

### AC1: Admin can create category with valid data

**Given** an authenticated admin  
**And** the category data is valid  
**When** the admin creates a `ProductCategory`  
**Then** the category is persisted  
**And** the created category is returned in the response

### AC2: Non-admin cannot create category

**Given** an authenticated non-admin user  
**When** the user attempts to create a `ProductCategory`  
**Then** the request is rejected  
**And** the system returns an authorization error

### AC3: Anonymous caller cannot create category

**Given** an unauthenticated caller  
**When** the caller attempts to create a `ProductCategory`  
**Then** the request is rejected as unauthenticated

### AC4: Category name is required

**Given** an authenticated admin  
**When** the admin attempts to create a category without a name  
**Then** the request is rejected

### AC5: Duplicate category is handled explicitly

**Given** an authenticated admin  
**And** a category with the same business identity already exists  
**When** the admin attempts to create the same category again  
**Then** the system rejects the request with a clear business error

## Suggested Domain Rules

- `ProductCategory` must have identity
- category name must not be blank
- category name should be normalized consistently before uniqueness checks
- duplicate categories should not be silently created
- category creation is an admin-only action

## Suggested API Shape

- `POST /api/admin/product-categories`

## Suggested Request Shape

```json
{
  "name": "Propane Bottles"
}
```

## Suggested Response Shape

```json
{
  "id": "generated-id",
  "name": "Propane Bottles"
}
```

## Domain Notes

- `ProductCategory` should be treated as a domain concept, not a DTO
- DTOs must exist only at the controller boundary
- domain logic belongs in `core`
- persistence model must be separate from domain model
- aggregate root discipline should be preserved

## Technical Notes for AI Implementation

- controller must be thin
- controller accepts DTO and maps to domain command
- service enforces authorization via `RbacService`
- service performs creation use case
- repository persists the aggregate root
- persistence maps domain ↔ entity explicitly
- JPA entity must be named `ProductCategoryEntity`

## Out of Scope / Must Verify

- whether category name must be globally unique or unique within a tenant/store
- whether slug/code is required in addition to name
- whether soft delete exists and affects uniqueness
