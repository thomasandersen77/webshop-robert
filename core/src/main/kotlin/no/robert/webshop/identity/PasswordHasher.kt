package no.robert.webshop.identity

/**
 * Port for passordhashing og verifisering. Implementasjon med BCrypt (eller annet) ligger utenfor core.
 */
interface PasswordHasher {
    fun hash(plainPassword: String): String

    fun matches(plainPassword: String, passwordHash: String): Boolean
}
