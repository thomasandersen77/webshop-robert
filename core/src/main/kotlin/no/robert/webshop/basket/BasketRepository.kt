package no.robert.webshop.basket

interface BasketRepository {
    fun findByCustomerId(customerId: String): Basket?
    fun save(basket: Basket): Basket
    fun deleteAll()
}
