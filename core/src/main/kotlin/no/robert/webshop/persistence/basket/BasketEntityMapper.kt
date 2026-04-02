package no.robert.webshop.persistence.basket

import no.robert.webshop.Money
import no.robert.webshop.basket.Basket
import no.robert.webshop.basket.BasketItem
import java.util.UUID

fun BasketEntity.toDomain(itemEntities: List<BasketItemEntity>): Basket =
    Basket(
        id = id,
        customerId = customerId,
        items = itemEntities.map { it.toDomain() },
        currency = currency,
    )

fun Basket.toEntity(): BasketEntity =
    BasketEntity(
        id = id,
        customerId = customerId,
        currency = currency,
    )

fun BasketItemEntity.toDomain(): BasketItem =
    BasketItem(
        productId = productId,
        quantity = quantity,
        unitPrice = Money(unitPriceMinor, currency),
    )

fun BasketItem.toEntity(basketId: String): BasketItemEntity =
    BasketItemEntity(
        id = UUID.randomUUID().toString(),
        basketId = basketId,
        productId = productId,
        quantity = quantity,
        unitPriceMinor = unitPrice.amountMinor,
        currency = unitPrice.currency,
    )
