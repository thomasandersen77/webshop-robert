package no.robert.webshop.persistence.admin

import no.robert.webshop.Money
import no.robert.webshop.Product
import no.robert.webshop.ProductCategory
import no.robert.webshop.ProductCategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class ProductCategoryRepositoryAdapter(
    private val categoryJpaRepository: ProductCategoryJpaRepository,
    private val productJpaRepository: ProductJpaRepository
) : ProductCategoryRepository {

    override fun findAll(): List<ProductCategory> {
        val categories = categoryJpaRepository.findAll()
        val products = productJpaRepository.findAll()
        val productsByCategoryId = products.groupBy { it.categoryId }

        return categories.map { entity ->
            entity.toDomain(productsByCategoryId[entity.id] ?: emptyList())
        }
    }

    override fun findById(id: String): ProductCategory? {
        val category = categoryJpaRepository.findByIdOrNull(id) ?: return null
        val products = productJpaRepository.findByCategoryId(id)
        return category.toDomain(products)
    }

    private fun ProductCategoryEntity.toDomain(productEntities: List<ProductEntity>): ProductCategory {
        return ProductCategory(
            id = this.id,
            name = this.name,
            products = productEntities.map { it.toDomain() }
        )
    }

    private fun ProductEntity.toDomain(): Product {
        return Product(
            id = this.id,
            categoryId = this.categoryId,
            name = this.name,
            description = this.description,
            price = Money(this.priceMinor, this.currency),
            ratingStars = this.ratingStars
        )
    }
}
