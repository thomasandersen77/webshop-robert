package no.robert.webshop.basket

import no.robert.webshop.Product

interface BasketProductRepository {
    fun findById(productId: String): Product?
}
