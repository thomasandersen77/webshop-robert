package no.robert.webshop.admin

import no.robert.webshop.Product
import no.robert.webshop.ProductCategory

interface AdminRepository {
    fun findCategoryByName(name: String): ProductCategory?
    fun findCategoryById(id: String): ProductCategory?
    fun saveCategory(category: ProductCategory): ProductCategory
    fun saveProduct(product: Product): Product
    fun deleteAllProducts()
    fun deleteAllCategories()
}