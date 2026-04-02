---
apply: always
---

# Webshop Backend Master Rules (Canonical)

> **Source of Truth:** Functional requirements are defined in `docs/kravspesifikasjon-webshop.md`.
> If any rule or prompt conflicts with the requirements, **the requirements document wins**.
> When in doubt: implement according to the requirements and **document the assumption** in code/PR.
> Documentation policy, if present elsewhere in the repo, must never override the requirement spec.
> This document is the canonical VIBE coding instruction set for the AI assistant working on the webshop backend.

---

## 0. Rule Contract (Always Enforced)

These rules must be followed for every change:

1. **Magnificent Monolith**
    - `api` (Maven module): HTTP boundary only
    - `core`: domain model and business logic
    - `payment`: payment provider integrations and payment implementations
    - persistence/infrastructure: JPA entities, repository adapters, database integration
    - No business logic in controllers
    - No infrastructure leakage into core

2. **Layering (Mandatory)**
    - Controller → Application/Domain Service
    - Service → Repository and/or Port
    - Repository Adapter → JPA / database
    - Payment provider implementations are invoked through ports/interfaces defined by core

3. **Mandatory Mapping Between Layers**
    - Mapping is required between every architectural layer
    - Write flow must be:
        - `DTO -> Domain -> Entity`
    - Read flow must be:
        - `Entity -> Domain -> DTO`
    - No layer may skip its mapping boundary
    - Controllers map DTO ↔ domain
    - Repository adapters map domain ↔ entity

4. **Ultra-thin Controllers**
    - Controllers do:
        - routing
        - auth context extraction
        - syntactic validation
        - DTO ↔ domain mapping
        - HTTP response shaping
    - Controllers do **not**:
        - perform business logic
        - access repositories directly
        - call Stripe SDK/client directly
        - make authorization decisions beyond coarse endpoint protection

5. **Core Isolation**
    - `core` MUST NOT depend on `api`
    - `core` MUST NOT depend on Stripe SDKs or external infrastructure SDKs
    - `core` MUST NOT depend on JPA entities
    - `core` MUST NOT depend on Spring MVC, Spring Security, servlet APIs, or request context
    - `core` defines ports/interfaces
    - `payment` implements payment-related ports
    - persistence/infrastructure implements repository adapters and entity mapping

6. **DTO Boundary Rule**
    - DTOs exist only at the API boundary
    - DTOs are only input/output to and from controllers
    - Services operate on domain objects, commands, value objects, and aggregates
    - Repositories return domain objects, never DTOs
    - DTOs must never appear in `core`
    - DTOs must never be used as repository models

7. **Domain Type Rule**
    - `core` shall only use types from the domain model
    - `core` shall use rich domain types, value objects, aggregates, commands, and domain services
    - Avoid primitive obsession where richer domain types improve clarity and invariants
    - Avoid anemic domain models

8. **Aggregate Root Rule (Critical)**
    - Aggregate root design is mandatory and extremely important
    - Every repository represents an aggregate root boundary
    - All writes must go through the aggregate root
    - Child entities must not be modified directly from outside the aggregate
    - Query and mutation paths must be designed around aggregate roots
    - Avoid multiple competing write paths for the same business concept
    - Aggregate root design should optimize for simplicity, consistency, and maintainability

9. **Transactions**
    - All state-changing service methods MUST be `@Transactional`
    - Read-only queries should be marked `@Transactional(readOnly = true)` when appropriate

10. **Database Migration Ownership**
    - Liquibase YAML changelogs define DB structure
    - Existing changesets are **immutable**
    - Never edit old changesets after they are committed
    - Add new numbered changesets for all schema evolution

11. **Security**
    - Controllers inject authenticated domain user via `@CurrentUser user: User`
    - Authorization must be enforced in the service layer via `RbacService`
    - Role model is intentionally simple: `ADMIN`, `CUSTOMER`
    - Authenticated user/context passed into `core` must be a domain type

12. **Business Language**
    - Log messages intended for business/operations must be in **Norwegian**
    - Exception messages intended for logs or users must be in **Norwegian**
    - Class names, package names, method names, variables, and code identifiers remain in English

13. **Testing**
    - Core unit tests: MockK, no Spring context
    - API tests: MockMvc **only for HTTP contract** (routing, validation messages, security at boundary)—not a substitute for `core` integration tests
    - Integration tests (Liquibase + JPA + transactions + business flows): **`core`** module test sources (or dedicated IT module)—see §13.0
    - Payment tests must support both Stripe-backed flows and mock payment flows

14. **Payment Strategy**
    - Payment in production uses Stripe
    - A mock payment implementation must exist for local development, demos, and tests
    - Business logic must depend on a payment port, not on Stripe-specific classes

15. **Code Quality Baseline**
    - SOLID is mandatory
    - DDD is mandatory
    - Thin controllers are mandatory
    - Core owns business rules
    - Prefer explicitness over cleverness
    - Keep modules cohesive and focused

16. **API (HTTP) vs `core` (services and integration tests)**
    - **`api`** is **only** the web boundary: REST **routing**, controllers, request/response DTOs, syntactic validation, DTO ↔ domain mapping **at the edge**, Spring Security / servlet filters, global HTTP exception mapping, OpenAPI, and other **HTTP-only** infrastructure. **`api` MUST NOT** contain application or domain **use-case `@Service`** classes (orchestration belongs in **`core`**).
    - **`core`** holds **all** application and domain **services** (`@Service` use cases), the domain model, commands, ports, repository **interfaces**, and `RbacService`-driven authorization for business operations.
    - **Integration tests** that verify **business behavior** together with **database** (Liquibase, JPA adapters, transactions, domain ↔ entity mapping) **MUST** live under **`core/src/test`** (or a dedicated integration-test Maven module that depends on `core` + persistence), **not** only under `api`. **`api/src/test`** is for **HTTP contract** tests only (e.g. MockMvc: routes, status codes, security filters, request/response shape)—**not** the primary place for full-stack business integration suites.

---

## 1. System Architecture (Magnificent Monolith)

### 1.1 Modules

Backend code lives under the Maven aggregator **`webshop_backend`** (`groupId` `com.robert.webshop`) with artifact modules **`api`**, **`core`**, and **`payment`**. Kotlin code uses the package prefix `no.robert.webshop` (for example `no.robert.webshop.payment.PaymentPort`).

- **`api`**
    - REST controllers (**routing** and delegation only)
    - Request/response DTOs
    - API mapping layer (DTO ↔ domain **at the HTTP boundary**)
    - Spring Security config (web layer)
    - Current user resolver
    - Global exception mapping (HTTP)
    - OpenAPI configuration
    - HTTP-specific validation
    - **Does not** contain application/use-case `@Service` classes (those live in **`core`**)

- **`core`**
    - Domain entities and value objects
    - Aggregates
    - Domain services
    - Application services / use-case services (**all `@Service` orchestration**)
    - Repository interfaces
    - External ports/interfaces
    - Authorization abstraction usage (`RbacService`)
    - Domain events if needed
    - **Primary home** for **integration tests** that exercise services + persistence + Liquibase (see §13)

- **persistence / infrastructure**
    - JPA entities
    - Spring Data repositories / repository adapters
    - Domain ↔ entity mappers
    - database-specific persistence concerns

- **`payment`**
    - Stripe integration
    - Mock payment implementation
    - Payment provider clients/adapters
    - Webhook verification adapters
    - Implementations of payment ports defined in core

### 1.2 Dependency Direction

- `api` depends on `core`
- `payment` depends on `core`
- persistence/infrastructure depends on `core`
- `core` depends on neither `api`, persistence frameworks, nor payment SDKs
- No circular dependencies
- No direct dependency from `api` to Stripe logic except through service contracts

### 1.3 Architectural Style

This project is a **modular monolith**, not microservices.

That means:

- clear module boundaries
- strong package discipline
- internal cohesion
- single deployable runtime
- no premature distributed complexity

Do not split into services unless explicitly required by the requirements document.

---

## 2. Layering: Controller → Service → Repository/Port

### 2.1 Controllers

Controllers must:

- accept HTTP requests
- validate syntactic correctness
- map request DTOs to service commands or domain input
- inject `@CurrentUser`
- delegate immediately to service layer
- map service/domain results into response DTOs
- return correct HTTP codes

Controllers must never:

- contain business rules
- talk to repositories
- construct domain decisions
- call Stripe or mock payment adapters directly
- enforce fine-grained RBAC inline
- calculate totals, discounts, stock reservation rules, or order state transitions

### 2.2 Services

Application and domain **use-case services** are implemented in the **`core`** Maven module only—not in **`api`**.

Services must:

- orchestrate use cases
- enforce authorization via `RbacService`
- enforce invariants
- coordinate repositories and ports
- own business workflows such as:
    - cart checkout
    - inventory validation
    - order creation
    - payment session creation
    - payment confirmation
    - cancellation/refund orchestration
    - admin product management

Services must not:

- accept transport DTOs
- return transport DTOs
- know about HTTP
- depend on Spring MVC concerns
- depend on JPA entities

### 2.3 Repositories

Repositories must:

- represent persistence boundaries for aggregate roots
- return domain objects
- hide JPA details from services
- keep query methods focused and domain-relevant

Repositories must not:

- return API DTOs
- expose JPA entities to `core`
- implement business rules
- become generic dumping grounds

### 2.4 Persistence Adapters

Persistence adapters must:

- map domain ↔ entity explicitly
- persist aggregate roots through repository boundaries
- keep entity-specific persistence concerns out of core

Persistence adapters must not:

- leak `*Entity` types into `core`
- bypass aggregate boundaries casually
- embed business rules that belong in domain services or aggregates

### 2.5 Ports

Ports belong in core and define required external behaviors such as:

- payment session creation
- payment status lookup
- payment confirmation
- optional refund execution
- email sending
- image storage coordination if later introduced

Implementations of ports live outside core.

---

## 3. SOLID Principles (Practical Enforcement)

### 3.1 SRP

- Controller: HTTP boundary only
- Service: one business capability/use case
- Repository: persistence boundary for one aggregate/root area
- Repository adapter: mapping and persistence only
- Payment adapter: one external integration responsibility

### 3.2 OCP

- Extend by introducing new strategies or implementations behind interfaces
- Example:
    - `PaymentPort` (or similar) interface in `core` — e.g. `no.robert.webshop.payment.PaymentPort`
    - Production adapter (e.g. `PaymentPortAdapter`) in `payment` under `no.robert.webshop`
    - Mock adapter (e.g. `MockPaymentPortAdapter`) in `payment` under `no.robert.webshop.mock`

### 3.3 LSP

- Mock payment implementation must behave consistently with the production payment contract
- Tests should be able to swap Stripe and mock flows without changing business semantics

### 3.4 ISP

- Keep interfaces small
- Prefer:
    - `CheckoutPaymentPort`
    - `PaymentStatusPort`
    - `RefundPaymentPort`
- Avoid giant all-purpose payment interfaces unless domain simplicity genuinely warrants it

### 3.5 DIP

- Core depends on abstractions
- Integration modules depend on core
- Spring wires implementations

---

## 4. Domain-Driven Design (DDD)

### 4.1 Ubiquitous Language

Use language aligned with the webshop domain:

- Customer
- Admin
- Product
- Category
- Inventory
- Stock Reservation
- Cart
- Order
- Order Line
- Checkout
- Payment
- Price
- Discount Rule
- Batch Price / Bulk Price
- Product Image

Use English in code identifiers, Norwegian in operational messages.

### 4.2 Suggested Bounded Contexts / Domain Areas

At minimum, organize core around these domain areas:

- **Catalog**
    - Product
    - Category
    - ProductImage
    - ProductStatus

- **Pricing**
    - Money
    - ProductPrice
    - BulkPricingRule
    - DiscountPolicy

- **Inventory**
    - InventoryItem
    - StockLevel
    - StockReservation

- **Ordering**
    - Cart
    - CartLine
    - Order
    - OrderLine
    - Checkout

- **Payment**
    - Payment
    - PaymentAttempt
    - PaymentStatus
    - PaymentReference

- **Identity & Access**
    - User
    - Role
    - RbacService

### 4.3 Rich Domain Model Rule

The core domain model must be rich, not anemic.

That means:

- aggregates protect invariants
- entities protect state transitions where natural
- value objects model concepts explicitly
- commands and services express use cases clearly
- business concepts should not collapse into primitive types unless there is a strong reason

### 4.4 Entities

Entities represent concepts with identity, for example:

- `User`
- `Product`
- `Category`
- `Order`
- `Payment`
- `InventoryItem`

Entities should protect their own invariants where natural.

Do not make entities mere bags of getters/setters unless there is a very strong reason.

### 4.5 Value Objects

Use immutable value objects for concepts such as:

- `Money`
- `ProductName`
- `Sku`
- `EmailAddress`
- `Quantity`
- `PaymentReference`
- `StripeSessionId`
- `OrderNumber`

If it has no identity and is conceptually atomic, prefer a value object.

### 4.6 Aggregates

Treat aggregate boundaries seriously.

Likely aggregate roots include:

- `Product`
- `Order`
- `Payment`
- `User`

Rules:

- access aggregates through repository interfaces of the root
- all writes go through the aggregate root
- do not modify child entities from outside aggregate logic
- avoid multiple write paths for the same aggregate
- aggregate root design is a primary simplification mechanism in this codebase

### 4.7 Services

Use:

- **Domain Services** for business logic that does not fit one entity
- **Application Services** for orchestration across aggregates and ports

Examples:

- `CheckoutService`
- `AdminProductService`
- `InventoryService`
- `PaymentService`
- `OrderQueryService`

---

## 5. Identity, Security, and Access Control

### 5.1 Authentication

Use Spring Security.

Authenticated users must be resolved into the domain user via:

```kotlin
package no.robert.webshop.api

fun someEndpoint(@CurrentUser user: User)
```

Controllers should not manually parse authentication details if `@CurrentUser` exists.

### 5.2 Authorization

Authorization must be enforced in the service layer with `RbacService`.

Examples:

* customer can manage own cart and own orders
* customer can view only own order history
* admin can manage products, categories, stock, and inspect orders
* admin can perform payment administration tasks if requirements allow it

Do not hardcode authorization rules inside controllers.

### 5.3 Annotation Policy

The current-user annotation mechanism is preferred.

Rules:

* annotate controller methods with `@CurrentUser user: User`
* pass user into the service layer
* use `RbacService` in services for authorization checks
* keep endpoint-level annotations simple and coarse
* use service-level authorization for actual domain access decisions

### 5.4 Preferred Auth Context Pattern

Preferred pattern:

* resolve authenticated user in `api`
* inject via `@CurrentUser`
* map to domain `User` or authenticated domain actor
* pass explicitly into service methods
* enforce authorization in `RbacService`

Avoid:

* singleton current-user holders
* hidden thread-local access from core
* static access patterns for authenticated user
* direct Spring Security types in `core`

### 5.5 Role Model

Current role model is intentionally simple:

* `CUSTOMER`
* `ADMIN`

Do not add roles unless required by the requirements document.

### 5.6 Security Design Principle

Authentication identifies.
Authorization decides.

The authenticated principal alone is not enough; service methods must verify what the user is allowed to do.

---

## 6. Payment Architecture

### 6.1 Production Payment

Production payment provider is Stripe.

Stripe concerns must be isolated behind ports in core.

Core must not know Stripe SDK types.

### 6.2 Mock Payment

A mock payment implementation is mandatory.

Use it for:

* local development
* fast demos
* deterministic tests
* offline workflows
* frontend integration before live payment is ready

The mock payment provider must simulate realistic outcomes:

* created session
* successful payment
* failed payment
* cancelled payment
* optional delayed confirmation flow

### 6.3 Payment Domain Rules

The source of truth for order payment state is the backend domain model, updated by verified payment events/workflows.

Never trust frontend redirect alone as proof of payment.

Payment confirmation should typically be handled through:

* verified webhook processing
* or explicit verified payment status lookup

### 6.4 Stripe Webhooks

Webhook processing belongs in API/payment integration layers, but the business effect belongs in core services.

Typical flow:

* webhook arrives
* signature is verified
* event is parsed
* relevant application service is called
* domain payment/order state is updated transactionally

### 6.5 Checkout Rule

Checkout business flow must live in core:

* validate cart
* validate stock
* calculate totals
* create order/payment draft
* request payment session through payment port
* persist references
* return checkout/session data to API

### 6.6 Payment Module Responsibility

`payment` may contain:

* Stripe client/adapters
* mock payment implementation
* payment config
* webhook signature verification adapter

It must not absorb core business logic.

---

## 7. Money Handling (CRITICAL)

### 7.1 Internal Representation

All money in domain logic and database is represented as `Long` in minor units.

For NOK, that means øre.

Never use:

* `Double`
* `Float`

for money.

### 7.2 Money Value Object

A domain value object for money is recommended:

```kotlin
package no.robert.webshop.domain

data class Money(
    val minor: Long,
    val currency: String = "NOK"
)
```

Add behavior where useful:

* plus
* minus
* multiply by quantity
* comparison
* zero checks

### 7.3 API Representation

All money exposed in API payloads should use a DTO like:

```kotlin
package no.robert.webshop.api.dto

data class MoneyDto(
    val minor: Long,
    val display: String,
    val currency: String = "NOK"
)
```

Rules:

* `minor` is canonical
* `display` is server-formatted
* frontend must not divide by 100
* formatting must avoid floating point arithmetic

### 7.4 Pricing Domain

Pricing logic belongs in core.

Examples:

* unit price
* bulk price
* discount thresholds
* campaign prices
* order line total
* order grand total

Controllers must never calculate prices.

---

## 8. Database, Persistence, and Liquibase

### 8.1 Liquibase Is Master

* Liquibase YAML is the schema source of truth
* Existing changesets are immutable
* Add new numbered files for schema changes

### 8.2 Synchronization Rule

Any JPA entity change requiring schema updates must be accompanied by a Liquibase changeset in the same commit/PR.

### 8.3 Naming Rules

* DB: snake_case
* Kotlin: camelCase
* All JPA entities must be suffixed with `Entity`

Examples:

* `ProductEntity`
* `OrderEntity`
* `UserEntity`

Domain objects in `core` must **not** be suffixed with `Entity`.

### 8.4 JPA Best Practices in Kotlin

* Do not use data class for JPA entities
* Keep entities persistence-safe
* Use open support through plugin configuration, not manual boilerplate where the plugin handles it
* Avoid lazy-loading pitfalls in `equals`, `hashCode`, and `toString`

### 8.5 Entity Boundary Rule

JPA entities belong only in persistence/infrastructure.

Rules:

* `*Entity` classes must never leak into `core`
* `*Entity` classes must never be exposed from controllers
* mapping between entity and domain must be explicit
* JPA entities are persistence models, not domain models

### 8.6 Repository Ownership

Keep persistence model aligned with aggregate roots.

Do not create repository methods that bypass aggregate boundaries casually.

---

## 9. Error Handling

### 9.1 Centralized Mapping

Use a centralized `GlobalExceptionHandler` in `api`.

Map domain/application exceptions to HTTP there.

### 9.2 Domain Exceptions

Domain and application services should throw meaningful exceptions when invariants or permissions fail.

Examples:

* `ProductNotFoundException`
* `OrderNotFoundException`
* `UnauthorizedOrderAccessException`
* `InsufficientStockException`
* `PaymentStateException`

### 9.3 Language

Exception messages intended for logs or end users must be in Norwegian.

Example:

```kotlin
throw IllegalStateException("Ordren kan ikke betales fordi handlekurven er tom")
```

### 9.4 Do Not Leak HTTP into Core

Core must not throw `ResponseStatusException` or similar HTTP-specific exceptions.

---

## 10. Logging & Observability

### 10.1 Language

Business/functional log lines must be in Norwegian.

### 10.2 MDC

Include at minimum:

* `correlationId`
* `actorId` or equivalent user identity

for request-correlated logs.

### 10.3 Levels

* `INFO`: key business events
* `WARN`: recoverable business issues
* `ERROR`: unexpected failures or critical issues

### 10.4 Kotlin Logging Style

Prefer Kotlin string interpolation.

Use:

```kotlin
logger.info("Ordre ${order.id} ble opprettet for bruker ${user.id}")
```

Avoid placeholder-heavy style if the project standard is interpolation.

### 10.5 Sensitive Data Rule

Never log:

* card data
* secrets
* Stripe secret keys
* raw authentication tokens
* sensitive personal data unnecessarily

---

## 11. API Design Rules

### 11.1 REST Style

The public API should be coherent and resource-oriented.

Examples:

* `/api/products`
* `/api/categories`
* `/api/cart`
* `/api/orders`
* `/api/admin/products`
* `/api/admin/inventory`
* `/api/payments/checkout`
* `/api/payments/webhooks/stripe`

### 11.2 DTOs

Keep DTOs explicit.

Do not expose internal entities directly.

Prefer request/response DTOs over generic mutable blobs.

### 11.3 Validation

Use Bean Validation / Jakarta Validation for syntactic constraints.

Examples:

* required fields
* string lengths
* positive quantities
* email format

Business validation stays in core.

### 11.4 OpenAPI

If OpenAPI is used, it must reflect real DTO contracts.

Breaking DTO changes require OpenAPI updates and test updates.

---

## 12. Coding Style (Kotlin + Spring)

### 12.1 General

Prefer:

* small focused classes
* immutable data where possible
* constructor injection
* explicit names
* module-internal visibility with `internal` where appropriate

Avoid:

* god services
* util classes full of unrelated functions
* static-style dumping grounds
* mixing web concerns into domain services

### 12.2 Kotlin Idioms

Use idiomatic Kotlin:

* extension functions where appropriate
* top-level functions when meaningful
* sealed class for constrained result/status models when useful
* smart casts with `is`
* safe casts with `as?`
* `let`, `run`, `also`, `apply` thoughtfully, not excessively

### 12.3 Clean Code Rules

* functions should be short and intention-revealing
* names must describe business intent
* no hidden side effects
* prefer one level of abstraction per function
* duplication should be removed when it clarifies the design

### 12.4 Package-by-Feature Bias

Prefer packaging by feature/domain area over packaging by technical layer only.

Example in core:

```text
core/
  catalog/
  ordering/
  inventory/
  payment/
  identity/
  pricing/
```

---

## 13. Testing Strategy & Quality

### 13.0 Module placement (mandatory)

* **Application/domain `@Service` classes** → **`core`** only. Never place use-case services in **`api`**.
* **Integration tests** (real or embedded DB, Liquibase, JPA repository adapters, transactional behavior, end-to-end **business** flows through **`core` services**) → **`core/src/test`** or a dedicated **`integration-tests`** (or similar) Maven module. Wire a minimal Spring test context that includes `core` + persistence beans **without** turning `api` into the only place that proves business correctness.
* **`api/src/test`** → **HTTP-layer** tests only: MockMvc (or equivalent) for controller mapping, status codes, filter chain / security behavior, and JSON contracts. Keep these **narrow**; do **not** maintain the **sole** copy of deep business+persistence integration coverage here.

### 13.1 Core Unit Tests

Mandatory for business logic.

Rules:

* MockK
* no Spring context
* fast, isolated, deterministic
* verify pricing, inventory, authorization, checkout, and payment state transitions

### 13.2 API (HTTP contract) tests

In **`api`**, use MockMvc (or equivalent) **only** to verify the **web boundary**:

* routing and handler mapping
* DTO validation and error responses
* HTTP status codes
* `@CurrentUser` / resolver behavior at the controller edge
* security filters and authenticated vs anonymous access
* request/response JSON shape

Do **not** treat **`api`** as the primary module for **full business + database integration**; that belongs under **`core`** (§13.0, §13.3).

### 13.3 Core integration tests

In **`core`** (or dedicated IT module), use H2 or Testcontainers to verify:

* Liquibase alignment
* repository adapter behavior and entity ↔ domain mapping
* transactions and consistency
* **full use-case flows** through **`core` services** (with test doubles for external ports where needed)

### 13.4 Payment Tests

Payment flows must be tested at multiple levels:

* unit tests for payment orchestration in core
* adapter tests for Stripe/mock implementations
* integration tests for webhook-to-service flow where relevant

### 13.5 Mock Payment Testability

The mock payment implementation must be easy to control in tests.

Example capabilities:

* mark next payment as success
* mark next payment as failure
* simulate callback/webhook completion

### 13.6 Known MockMvc Rule for `@CurrentUser`

If MockMvc tests fail because `@CurrentUser` resolves to null:

* ensure the argument resolver is registered
* prefer full web application context setup
* ensure the user repository/service returns the correct domain user

---

## 14. Business Rules Placement

The following logic belongs in core, never in API:

* who may access which order
* who may manage products
* cart calculation
* order total calculation
* stock validation
* stock reservation/release
* bulk pricing
* discount decisions
* payment state transitions
* order state transitions
* product publication decisions
* admin/customer visibility rules

API can only validate syntax and delegate.

---

## 15. Suggested Domain Rules for the Webshop

These are implementation biases unless overridden by the requirements document.

### 15.1 Customer Rules

A customer may:

* browse published products
* manage own cart
* checkout own cart
* view own orders
* initiate payment for own order when allowed

A customer may not:

* manage catalog
* inspect other customers’ orders
* modify inventory
* access admin endpoints

### 15.2 Admin Rules

An admin may:

* manage categories
* manage products
* manage prices
* manage bulk pricing rules
* manage inventory
* inspect orders
* inspect payment states
* trigger admin workflows allowed by requirements

### 15.3 Order Rules

An order should generally:

* be created from a validated cart
* contain immutable pricing snapshots at checkout time
* not trust current catalog price after order creation
* move through explicit statuses

### 15.4 Inventory Rules

Inventory must be controlled in core.

Typical rules:

* prevent selling unavailable stock unless backorder is explicitly supported
* reserve stock at the correct stage if required by the business
* release stock on failed/cancelled order flows if required

### 15.5 Product Rules

Products should support:

* category membership
* publication state
* price
* optional bulk pricing
* images
* localized text later if needed

---

## 16. Source of Truth and Requirement-Driven Development

### 16.1 Source of Truth

Functional requirements live in:

* `docs/kravspesifikasjon-webshop.md`

### 16.2 Workflow

Before implementing:

* verify the requirement
* identify the domain area
* choose the module correctly
* define whether logic belongs in API, core, persistence, or payment
* identify the aggregate root boundary
* identify DTO ↔ domain ↔ entity mappings

After implementing:

* verify behavior against the requirement
* verify tests
* verify architectural compliance with this file

### 16.3 Conflict Resolution

If any instruction conflicts with the requirement spec:

* follow `docs/kravspesifikasjon-webshop.md`
* document assumptions clearly

---

## 17. AI Assistant Execution Rules (VIBE Coding Mode)

These are direct instructions to the AI assistant.

### 17.1 Before Writing Code

Always determine:

1. Which module owns the change:

    * `api`
    * `core`
    * persistence/infrastructure
    * `payment`
2. Whether the change is:

    * HTTP boundary
    * business logic
    * persistence
    * payment integration
    * security
3. Whether a new abstraction/port is needed
4. What the aggregate root boundary is
5. What the required mappings are:

    * DTO ↔ domain
    * domain ↔ entity

### 17.2 When Adding Endpoints

The AI assistant must:

* create ultra-thin controllers
* use request/response DTOs
* inject `@CurrentUser` where authenticated context is needed
* delegate to service layer immediately
* avoid embedding business logic
* map DTO ↔ domain explicitly

### 17.3 When Adding Business Logic

The AI assistant must:

* place logic in `core`
* prefer domain-centric services and value objects
* enforce authorization via `RbacService`
* avoid DTOs in service signatures
* avoid JPA entities in core
* write unit tests first or alongside implementation
* preserve aggregate root discipline
* place **integration tests** for services + DB under **`core`** (§13.0), not only in **`api`**

### 17.4 When Adding Persistence

The AI assistant must:

* create or update `*Entity` classes in persistence/infrastructure only
* map domain ↔ entity explicitly
* keep repositories aligned with aggregate roots
* avoid leaking persistence concerns into `core`

### 17.5 When Adding Payment Functionality

The AI assistant must:

* define or use a payment port in core
* implement the adapter in `payment`
* keep Stripe-specific types out of core
* provide a mock implementation when relevant
* ensure webhook/business side effects are processed through core services

### 17.6 When Adding Security

The AI assistant must:

* use Spring Security
* use `@CurrentUser`
* keep authorization decisions in service layer
* avoid ad hoc permission checks in controllers
* pass authenticated user into core as a domain type

### 17.7 When Changing Database Structure

The AI assistant must:

* add a Liquibase changeset
* update JPA entities/repositories consistently
* never edit historical changesets
* ensure integration tests still pass

### 17.8 When Adding Tests

The AI assistant must:

* add **unit tests** for `core` services with MockK (no Spring) where practical
* add **integration tests** that prove Liquibase, persistence, transactions, and **business flows through `core` services** under **`core/src/test`** (or a dedicated IT module)—**not** only under `api`
* use **`api` tests** for **HTTP contract** only (MockMvc: routes, codes, security boundary, JSON shape)
* avoid making `api` the sole owner of deep database + use-case verification

### 17.9 When Unsure

The AI assistant must:

* prefer simpler design
* preserve module boundaries
* avoid leaking infrastructure into core
* document assumptions instead of inventing silent behavior

---

## 18. Anti-Patterns (Forbidden)

The following are forbidden unless explicitly justified and approved by the requirements:

* fat controllers
* DTOs in core service signatures
* repositories called directly from controllers
* Stripe SDK usage in controllers
* Stripe SDK usage in core
* business logic in mappers
* money as `Double` or `Float`
* editing old Liquibase changesets
* returning JPA entities directly from API
* authorization only in controllers
* giant god services with unrelated responsibilities
* generic Util dumping grounds
* leaking HTTP exceptions into domain/core
* duplicating payment logic across controller/service/adapter layers
* leaking `*Entity` types into `core`
* using DTOs as domain models
* using JPA entities as API contracts
* bypassing aggregate roots for writes
* singleton/global current-user access in domain/core
* application or domain **use-case `@Service`** classes in the **`api`** module
* **business + persistence integration tests** living **only** in **`api`** without equivalent coverage in **`core`**

---

## 19. Preferred Implementation Patterns

Examples below match the **`webshop_backend`** Maven layout: modules **`api`**, **`core`**, and **`payment`**, with Kotlin packages under **`no.robert.webshop`**.

### 19.1 Controller Example Shape

```kotlin
package no.robert.webshop.api.order

import no.robert.webshop.api.dto.OrderResponseDto
import no.robert.webshop.api.security.CurrentUser
import no.robert.webshop.identity.User
import no.robert.webshop.order.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService,
) {

    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable orderId: UUID,
        @CurrentUser user: User,
    ): OrderResponseDto {
        return orderService.getOrderForUser(orderId, user).toDto()
    }
}
```

### 19.2 Service Example Shape

```kotlin
package no.robert.webshop.order

import no.robert.webshop.Order
import no.robert.webshop.identity.RbacService
import no.robert.webshop.identity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val rbacService: RbacService,
) {

    @Transactional(readOnly = true)
    fun getOrderForUser(orderId: UUID, user: User): Order {
        val order = orderRepository.findById(orderId)
            ?: throw OrderNotFoundException("Fant ikke ordre")

        rbacService.assertCanViewOrder(user, order)

        return order
    }
}
```

### 19.3 Persistence Mapping Example Shape

```kotlin
package no.robert.webshop.catalog.persistence

import no.robert.webshop.catalog.Product
import no.robert.webshop.catalog.ProductName
import no.robert.webshop.pricing.Money

class ProductRepositoryAdapter(
    private val jpaRepository: ProductJpaRepository,
) : ProductRepository {

    override fun findById(id: ProductId): Product? =
        jpaRepository.findById(id.value).orElse(null)?.toDomain()

    override fun save(product: Product): Product =
        jpaRepository.save(product.toEntity()).toDomain()
}

fun ProductEntity.toDomain(): Product =
    Product.restore(
        id = ProductId(id),
        name = ProductName(name),
        price = Money(priceMinor, currency),
    )

fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id.value,
        name = name.value,
        priceMinor = price.minor,
        currency = price.currency,
    )
```

### 19.4 Payment Port Example Shape

```kotlin
package no.robert.webshop.payment

import no.robert.webshop.OrderSummary

interface PaymentPort {
    fun processPayment(order: OrderSummary): Boolean
}
```

---

## 20. Final Consistency Guarantees

To avoid contradictions, these rules are always true:

* controllers are thin
* DTOs stay in API only
* core owns business logic
* core uses only domain types
* persistence entities are suffixed with `Entity`
* mapping exists explicitly between DTO ↔ domain ↔ entity
* aggregate roots define write boundaries
* payment integration is isolated
* authorization is enforced in services
* money uses minor units
* Liquibase owns schema history
* Stripe is the production payment provider
* mock payment must exist
* Kotlin should stay idiomatic and explicit
* requirements document is authoritative
* `api` is HTTP/routing only; **services** and **primary integration tests** belong in **`core`**

---
