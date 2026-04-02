package no.robert.webshop.persistence.admin

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product_categories")
class ProductCategoryEntity(
    @Id
    @Column(length = 36, nullable = false)
    var id: String = "",

    @Column(nullable = false, unique = true, length = 255)
    var name: String = ""
)
