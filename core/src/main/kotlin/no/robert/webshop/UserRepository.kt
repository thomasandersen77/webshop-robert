package no.robert.webshop

/**
 * Persistensgrense for bruker-aggregatet. Implementasjon i api/persistence (JPA).
 */
interface UserRepository {
    fun findById(id: String): User?

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun save(user: User): User

    fun deleteAll()
}
