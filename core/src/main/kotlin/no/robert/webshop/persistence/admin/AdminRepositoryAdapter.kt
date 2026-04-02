package no.robert.webshop.persistence.admin

import no.robert.webshop.Money
import no.robert.webshop.Product
import no.robert.webshop.ProductCategory
import no.robert.webshop.admin.AdminRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class AdminRepositoryAdapter(
    private val categoryJpaRepository: ProductCategoryJpaRepository,
    private val productJpaRepository: ProductJpaRepository
) : AdminRepository {

    override fun findCategoryByName(name: String): ProductCategory? {
        return categoryJpaRepository.findByNameIgnoreCase(name)?.toDomain()
    }

    override fun findCategoryById(id: String): ProductCategory? {
        return categoryJpaRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun saveCategory(category: ProductCategory): ProductCategory {
        val entity = ProductCategoryEntity(
            id = category.id ?: UUID.randomUUID().toString(),
            name = category.name
        )
        return categoryJpaRepository.save(entity).toDomain()
    }

    override fun saveProduct(product: Product): Product {
        val entity = ProductEntity(
            id = product.id ?: UUID.randomUUID().toString(),
            categoryId = product.categoryId,
            name = product.name,
            description = product.description,
            priceMinor = product.price.amountMinor,
            currency = product.price.currency,
            ratingStars = product.ratingStars
        )
        return productJpaRepository.save(entity).toDomain()
    }
    
    override fun deleteAllProducts() {
        productJpaRepository.deleteAll()
    }
    
    override fun deleteAllCategories() {
        categoryJpaRepository.deleteAll()
    }

    private fun ProductCategoryEntity.toDomain(): ProductCategory {
        return ProductCategory(
            id = this.id,
            name = this.name
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
