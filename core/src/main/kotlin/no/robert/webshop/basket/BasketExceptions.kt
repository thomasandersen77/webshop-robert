package no.robert.webshop.basket

open class BasketException(message: String) : RuntimeException(message)

class BasketAlreadyExistsException(customerId: String) :
    BasketException("Handlekurv finnes allerede for bruker $customerId")

class BasketNotFoundException(customerId: String) :
    BasketException("Fant ikke handlekurv for bruker $customerId")

class BasketProductNotFoundException(productId: String) :
    BasketException("Fant ikke produkt med id $productId")

class BasketProductNotInBasketException(productId: String) :
    BasketException("Produkt med id $productId finnes ikke i handlekurven")
