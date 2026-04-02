package no.robert.webshop

interface ProductCategoryRepository {
    fun findAll(): List<ProductCategory>
    fun findById(id: String): ProductCategory?
}
