package no.robert.webshop.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import no.robert.webshop.Product
import no.robert.webshop.ProductCategory
import no.robert.webshop.User
import no.robert.webshop.admin.AdminService
import no.robert.webshop.admin.CreateProductCommand
import no.robert.webshop.security.CurrentUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService
) {

    @PostMapping("/product-categories")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProductCategory(
        @CurrentUser user: User,
        @RequestBody request: CreateCategoryRequest
    ): ProductCategoryResponse {
        val category = adminService.createProductCategory(user, request.name)
        return ProductCategoryResponse.from(category)
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(
        @CurrentUser user: User,
        @RequestBody request: CreateProductRequest
    ): ProductResponse {
        val product = adminService.createProduct(user, request.toCommand())
        return ProductResponse.from(product)
    }


}

data class CreateCategoryRequest(
    @param:JsonProperty("name")
    val name: String
)

data class CreateProductRequest(
    @param:JsonProperty("categoryId")
    val categoryId: String,
    @param:JsonProperty("name")
    val name: String,
    @param:JsonProperty("description")
    val description: String,
    @param:JsonProperty("priceMinor")
    val priceMinor: Int,
    @param:JsonProperty("ratingStars")
    val ratingStars: Int
) {
    fun toCommand() = CreateProductCommand(
        categoryId = categoryId,
        name = name,
        description = description,
        priceMinor = priceMinor,
        ratingStars = ratingStars
    )
}

data class ProductResponse(
    @param:JsonProperty("id")
    val id: String,
    @param:JsonProperty("categoryId")
    val categoryId: String,
    @param:JsonProperty("name")
    val name: String,
    @param:JsonProperty("description")
    val description: String,
    @param:JsonProperty("priceMinor")
    val priceMinor: Int,
    @param:JsonProperty("ratingStars")
    val ratingStars: Int
) {
    companion object {
        fun from(domain: Product): ProductResponse {
            return ProductResponse(
                id = domain.id ?: throw IllegalStateException("Product ID must be present"),
                categoryId = domain.categoryId,
                name = domain.name,
                description = domain.description,
                priceMinor = domain.priceMinor,
                ratingStars = domain.ratingStars
            )
        }
    }
}

data class ProductCategoryResponse(
    val id: String,
    val name: String
) {
    companion object {
        fun from(domain: ProductCategory): ProductCategoryResponse {
            return ProductCategoryResponse(
                id = domain.id ?: throw IllegalStateException("Category ID must be present"),
                name = domain.name
            )
        }
    }
}