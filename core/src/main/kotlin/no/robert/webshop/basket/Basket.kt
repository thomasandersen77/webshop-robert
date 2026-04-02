package no.robert.webshop.basket

import no.robert.webshop.Product
import java.util.UUID

data class Basket(
    val id: String,
    val customerId: String,
    val items: List<BasketItem> = emptyList(),
) {

    init {
        require(id.isNotBlank()) { "Handlekurv-ID kan ikke være tom" }
        require(customerId.isNotBlank()) { "Kunde-ID kan ikke være tom" }
    }

    fun addProduct(product: Product, quantity: Int): Basket {
        require(quantity > 0) { "Antall må være større enn 0" }
        val productId = product.id ?: throw IllegalArgumentException("Produkt må ha ID")
        val unitPriceMinor = product.priceMinor.toLong()
        require(unitPriceMinor >= 0) { "Produktpris kan ikke være negativ" }

        val existingLine = items.firstOrNull { it.productId == productId }
        val updatedItems = if (existingLine == null) {
            items + BasketItem(
                productId = productId,
                quantity = quantity,
                unitPriceMinor = unitPriceMinor,
            )
        } else {
            items.map { line ->
                if (line.productId == productId) line.increaseQuantity(quantity) else line
            }
        }

        return copy(items = updatedItems)
    }

    fun removeProduct(productId: String): Basket {
        require(productId.isNotBlank()) { "Produkt-ID kan ikke være tom" }
        if (items.none { it.productId == productId }) {
            throw BasketProductNotInBasketException(productId)
        }
        return copy(items = items.filterNot { it.productId == productId })
    }

    fun totalAmountMinor(): Long = items.sumOf { it.lineAmountMinor() }

    companion object {
        fun createForCustomer(customerId: String): Basket {
            return Basket(
                id = UUID.randomUUID().toString(),
                customerId = customerId,
                items = emptyList(),
            )
        }
    }
}

data class BasketItem(
    val productId: String,
    val quantity: Int,
    val unitPriceMinor: Long,
) {

    init {
        require(productId.isNotBlank()) { "Produkt-ID kan ikke være tom" }
        require(quantity > 0) { "Antall må være større enn 0" }
        require(unitPriceMinor >= 0) { "Enhetspris kan ikke være negativ" }
    }

    fun increaseQuantity(by: Int): BasketItem {
        require(by > 0) { "Økning i antall må være større enn 0" }
        return copy(quantity = quantity + by)
    }

    fun lineAmountMinor(): Long = unitPriceMinor * quantity
}
