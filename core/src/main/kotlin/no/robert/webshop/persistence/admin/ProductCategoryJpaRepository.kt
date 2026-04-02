package no.robert.webshop.persistence.admin

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryJpaRepository : JpaRepository<ProductCategoryEntity, String> {
    fun findByNameIgnoreCase(name: String): ProductCategoryEntity?
}
