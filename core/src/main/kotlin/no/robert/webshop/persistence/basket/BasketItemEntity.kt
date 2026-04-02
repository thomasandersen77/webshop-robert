package no.robert.webshop.persistence.basket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "basket_items")
class BasketItemEntity(
    @Id
    @Column(length = 36, nullable = false)
    var id: String = "",

    @Column(name = "basket_id", length = 36, nullable = false)
    var basketId: String = "",

    @Column(name = "product_id", length = 36, nullable = false)
    var productId: String = "",

    @Column(nullable = false)
    var quantity: Int = 0,

    @Column(name = "unit_price_minor", nullable = false)
    var unitPriceMinor: Long = 0L,

    @Column(name = "currency", nullable = false, length = 3)
    var currency: String = "NOK",
)
