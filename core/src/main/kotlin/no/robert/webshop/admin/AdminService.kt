package no.robert.webshop.admin

import no.robert.webshop.Product
import no.robert.webshop.ProductCategory
import no.robert.webshop.User
import no.robert.webshop.identity.RbacService
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val repository: AdminRepository,
    private val rbacService: RbacService
) {

    fun createProductCategory(user: User, name: String): ProductCategory {
        rbacService.checkAdmin(user)

        val normalizedName = name.trim()
        if (normalizedName.isBlank()) {
            throw IllegalArgumentException("Category name cannot be blank")
        }

        if (repository.findCategoryByName(normalizedName) != null) {
            throw DuplicateCategoryException("Category with name '$normalizedName' already exists")
        }

        val category = ProductCategory(name = normalizedName)
        return repository.saveCategory(category)
    }

    fun createProduct(user: User, command: CreateProductCommand): Product {
        rbacService.checkAdmin(user)

        validateProductCommand(command)

        repository.findCategoryById(command.categoryId)
            ?: throw ProductCategoryNotFoundException("Product category '${command.categoryId}' not found")

        val product = Product(
            categoryId = command.categoryId,
            name = command.name.trim(),
            description = command.description.trim(),
            priceMinor = command.priceMinor,
            ratingStars = command.ratingStars
        )

        return repository.saveProduct(product)
    }

    private fun validateProductCommand(command: CreateProductCommand) {
        if (command.name.trim().isBlank()) {
            throw IllegalArgumentException("Product name cannot be blank")
        }
        if (command.priceMinor < 0) {
            throw IllegalArgumentException("Price cannot be negative")
        }
        if (command.ratingStars !in 1..5) {
            throw IllegalArgumentException("Rating must be between 1 and 5 stars")
        }
    }

}

data class CreateProductCommand(
    val categoryId: String,
    val name: String,
    val description: String,
    val priceMinor: Int,
    val ratingStars: Int
)

class DuplicateCategoryException(message: String) : RuntimeException(message)

class ProductCategoryNotFoundException(message: String) : RuntimeException(message)