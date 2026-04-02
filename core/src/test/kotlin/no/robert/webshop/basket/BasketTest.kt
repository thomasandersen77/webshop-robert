package no.robert.webshop.basket

import no.robert.webshop.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class BasketTest {

    @Test
    fun `add product creates line and calculates total`() {
        val basket = Basket(id = "basket-1", customerId = "customer-1")
        val product = Product(
            id = "product-1",
            categoryId = "cat-1",
            name = "11kg Flaske",
            description = "Staalflaske",
            priceMinor = 29900,
            ratingStars = 5,
        )

        val updated = basket.addProduct(product, 2)

        assertEquals(1, updated.items.size)
        assertEquals(2, updated.items.first().quantity)
        assertEquals(59800L, updated.totalAmountMinor())
    }

    @Test
    fun `add same product increases quantity on existing line`() {
        val product = Product(
            id = "product-1",
            categoryId = "cat-1",
            name = "11kg Flaske",
            description = "Staalflaske",
            priceMinor = 29900,
            ratingStars = 5,
        )
        val basket = Basket(id = "basket-1", customerId = "customer-1")
            .addProduct(product, 1)

        val updated = basket.addProduct(product, 3)

        assertEquals(1, updated.items.size)
        assertEquals(4, updated.items.first().quantity)
        assertEquals(119600L, updated.totalAmountMinor())
    }

    @Test
    fun `remove product removes full line`() {
        val product = Product(
            id = "product-1",
            categoryId = "cat-1",
            name = "11kg Flaske",
            description = "Staalflaske",
            priceMinor = 29900,
            ratingStars = 5,
        )
        val basket = Basket(id = "basket-1", customerId = "customer-1")
            .addProduct(product, 2)

        val updated = basket.removeProduct("product-1")

        assertEquals(0, updated.items.size)
        assertEquals(0L, updated.totalAmountMinor())
    }

    @Test
    fun `remove unknown product throws explicit exception`() {
        val basket = Basket(id = "basket-1", customerId = "customer-1")

        assertThrows(BasketProductNotInBasketException::class.java) {
            basket.removeProduct("unknown-product")
        }
    }

    @Test
    fun `quantity must be positive when adding product`() {
        val product = Product(
            id = "product-1",
            categoryId = "cat-1",
            name = "11kg Flaske",
            description = "Staalflaske",
            priceMinor = 29900,
            ratingStars = 5,
        )
        val basket = Basket(id = "basket-1", customerId = "customer-1")

        assertThrows(IllegalArgumentException::class.java) {
            basket.addProduct(product, 0)
        }
    }
}
