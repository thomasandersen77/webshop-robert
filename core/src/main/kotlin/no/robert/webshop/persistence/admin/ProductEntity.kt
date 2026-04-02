package no.robert.webshop.persistence.admin

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class ProductEntity(
    @Id
    @Column(length = 36, nullable = false)
    var id: String = "",

    @Column(name = "category_id", length = 36, nullable = false)
    var categoryId: String = "",

    @Column(nullable = false, length = 255)
    var name: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String = "",

    @Column(name = "price_minor", nullable = false)
    var priceMinor: Long = 0,

    @Column(name = "currency", nullable = false, length = 3)
    var currency: String = "NOK",

    @Column(name = "rating_stars", nullable = false)
    var ratingStars: Int = 0
)
