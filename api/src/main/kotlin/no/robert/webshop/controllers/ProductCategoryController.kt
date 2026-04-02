package no.robert.webshop.controllers

import no.robert.webshop.Product
import no.robert.webshop.ProductCategory
import no.robert.webshop.ProductCategoryQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/product-categories")
class ProductCategoryController(
    private val queryService: ProductCategoryQueryService
) {

    @GetMapping
    fun getAllCategories(): List<ProductCategoryPublicResponse> {
        return queryService.getAllCategoriesWithProducts()
            .map { ProductCategoryPublicResponse.from(it) }
    }

    @GetMapping("/{categoryId}")
    fun getCategory(@PathVariable categoryId: String): ResponseEntity<ProductCategoryPublicResponse> {
        val category = queryService.getCategoryById(categoryId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ProductCategoryPublicResponse.from(category))
    }
}

data class ProductCategoryPublicResponse(
    val id: String,
    val name: String,
    val products: List<ProductPublicResponse>
) {
    companion object {
        fun from(domain: ProductCategory) = ProductCategoryPublicResponse(
            id = domain.id ?: "",
            name = domain.name,
            products = domain.products.map { ProductPublicResponse.from(it) }
        )
    }
}

data class ProductPublicResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: MoneyResponseDto,
    val ratingStars: Int
) {
    companion object {
        fun from(domain: Product) = ProductPublicResponse(
            id = domain.id ?: "",
            name = domain.name,
            description = domain.description,
            price = MoneyResponseDto.from(domain.price),
            ratingStars = domain.ratingStars
        )
    }
}