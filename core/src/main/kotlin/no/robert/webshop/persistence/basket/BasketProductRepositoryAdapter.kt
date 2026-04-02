package no.robert.webshop.persistence.basket

import no.robert.webshop.Product
import no.robert.webshop.basket.BasketProductRepository
import no.robert.webshop.persistence.admin.ProductEntity
import no.robert.webshop.persistence.admin.ProductJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BasketProductRepositoryAdapter(
    private val productJpaRepository: ProductJpaRepository,
) : BasketProductRepository {

    override fun findById(productId: String): Product? {
        return productJpaRepository.findByIdOrNull(productId)?.toDomain()
    }

    private fun ProductEntity.toDomain(): Product {
        return Product(
            id = id,
            categoryId = categoryId,
            name = name,
            description = description,
            priceMinor = priceMinor,
            ratingStars = ratingStars,
        )
    }
}
