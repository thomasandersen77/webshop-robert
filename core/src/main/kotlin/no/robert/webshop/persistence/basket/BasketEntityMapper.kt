package no.robert.webshop.persistence.basket

import no.robert.webshop.basket.Basket
import no.robert.webshop.basket.BasketItem
import java.util.UUID

fun BasketEntity.toDomain(itemEntities: List<BasketItemEntity>): Basket =
    Basket(
        id = id,
        customerId = customerId,
        items = itemEntities.map { it.toDomain() },
    )

fun Basket.toEntity(): BasketEntity =
    BasketEntity(
        id = id,
        customerId = customerId,
    )

fun BasketItemEntity.toDomain(): BasketItem =
    BasketItem(
        productId = productId,
        quantity = quantity,
        unitPriceMinor = unitPriceMinor,
    )

fun BasketItem.toEntity(basketId: String): BasketItemEntity =
    BasketItemEntity(
        id = UUID.randomUUID().toString(),
        basketId = basketId,
        productId = productId,
        quantity = quantity,
        unitPriceMinor = unitPriceMinor,
    )
