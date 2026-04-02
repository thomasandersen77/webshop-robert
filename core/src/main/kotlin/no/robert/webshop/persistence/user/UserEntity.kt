package no.robert.webshop.persistence.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
open class UserEntity(

    @Id
    @Column(length = 36, nullable = false)
    open var id: String = "",

    @Column(nullable = false, unique = true, length = 255)
    open var email: String = "",

    @Column(name = "password_hash", nullable = false, length = 255)
    open var passwordHash: String = "",

    @Column(nullable = false, length = 32)
    open var role: String = "",

    @Column(nullable = false)
    open var active: Boolean = true,
)
