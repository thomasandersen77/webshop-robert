package no.robert.webshop.persistence.basket

import org.springframework.data.jpa.repository.JpaRepository

interface BasketItemJpaRepository : JpaRepository<BasketItemEntity, String> {
    fun findByBasketId(basketId: String): List<BasketItemEntity>
    fun deleteByBasketId(basketId: String)
}
