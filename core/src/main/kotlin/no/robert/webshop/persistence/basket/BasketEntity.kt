package no.robert.webshop.persistence.basket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "baskets")
class BasketEntity(
    @Id
    @Column(length = 36, nullable = false)
    var id: String = "",

    @Column(name = "customer_id", length = 36, nullable = false, unique = true)
    var customerId: String = "",

    @Column(name = "currency", nullable = false, length = 3)
    var currency: String = "NOK",
)
