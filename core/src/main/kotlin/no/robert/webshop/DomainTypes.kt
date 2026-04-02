package no.robert.webshop

data class User(
    val id: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val active: Boolean = true,
)

enum class UserRole {
    CUSTOMER, ADMIN
}

data class Order(
    val id: String,
    val customerId: String,
    val items: List<OrderItem>
)

data class OrderItem(
    val productId: String,
    val quantity: Int
)

data class Customer(
    val id: String,
    val name: String
)

data class Product(
    val id: String? = null,
    val categoryId: String,
    val name: String,
    val description: String,
    val price: Money,
    val ratingStars: Int
)

data class ProductStock(
    val productId: String,
    val quantity: Int
)

data class ProductCategory(
    val id: String? = null,
    val name: String,
    val products: List<Product> = emptyList()
)

data class OrderSummary(
    val order: Order,
    val customer: Customer,
    val products: List<Product>
)