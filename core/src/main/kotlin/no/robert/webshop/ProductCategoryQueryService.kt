package no.robert.webshop

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductCategoryQueryService(
    private val productCategoryRepository: ProductCategoryRepository
) {

    @Transactional(readOnly = true)
    fun getAllCategoriesWithProducts(): List<ProductCategory> {
        return productCategoryRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getCategoryById(id: String): ProductCategory? {
        return productCategoryRepository.findById(id)
    }
}
