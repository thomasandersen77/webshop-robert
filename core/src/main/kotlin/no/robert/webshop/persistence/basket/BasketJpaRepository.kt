package no.robert.webshop.persistence.basket

import org.springframework.data.jpa.repository.JpaRepository

interface BasketJpaRepository : JpaRepository<BasketEntity, String> {
    fun findByCustomerId(customerId: String): BasketEntity?
}
