package no.robert.webshop.persistence.admin

import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductEntity, String> {
    fun findByCategoryId(categoryId: String): List<ProductEntity>
}
