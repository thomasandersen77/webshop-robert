package no.robert.webshop.persistence.user

import no.robert.webshop.User
import no.robert.webshop.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdapter(
    private val jpa: UserJpaRepository,
) : UserRepository {

    override fun findById(id: String): User? =
        jpa.findById(id).orElse(null)?.toDomain()

    override fun findByEmail(email: String): User? =
        jpa.findByEmail(email)?.toDomain()

    override fun existsByEmail(email: String): Boolean =
        jpa.existsByEmail(email)

    override fun save(user: User): User =
        jpa.save(user.toEntity()).toDomain()

    override fun deleteAll() {
        jpa.deleteAll()
    }
}
