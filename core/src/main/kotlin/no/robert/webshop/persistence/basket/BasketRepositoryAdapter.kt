package no.robert.webshop.persistence.basket

import no.robert.webshop.basket.Basket
import no.robert.webshop.basket.BasketRepository
import org.springframework.stereotype.Component

@Component
class BasketRepositoryAdapter(
    private val basketJpaRepository: BasketJpaRepository,
    private val basketItemJpaRepository: BasketItemJpaRepository,
) : BasketRepository {

    override fun findByCustomerId(customerId: String): Basket? {
        val basketEntity = basketJpaRepository.findByCustomerId(customerId) ?: return null
        val itemEntities = basketItemJpaRepository.findByBasketId(basketEntity.id)
        return basketEntity.toDomain(itemEntities)
    }

    override fun save(basket: Basket): Basket {
        val savedBasket = basketJpaRepository.save(basket.toEntity())
        basketItemJpaRepository.deleteByBasketId(savedBasket.id)
        val itemEntities = basket.items.map { it.toEntity(savedBasket.id) }
        basketItemJpaRepository.saveAll(itemEntities)
        val savedItems = basketItemJpaRepository.findByBasketId(savedBasket.id)
        return savedBasket.toDomain(savedItems)
    }

    override fun deleteAll() {
        basketItemJpaRepository.deleteAll()
        basketJpaRepository.deleteAll()
    }
}
