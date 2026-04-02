package no.robert.webshop.persistence.user

import no.robert.webshop.User
import no.robert.webshop.UserRole

fun UserEntity.toDomain(): User =
    User(
        id = id,
        email = email,
        passwordHash = passwordHash,
        role = UserRole.valueOf(role),
        active = active,
    )

fun User.toEntity(): UserEntity =
    UserEntity(
        id = id,
        email = email,
        passwordHash = passwordHash,
        role = role.name,
        active = active,
    )
