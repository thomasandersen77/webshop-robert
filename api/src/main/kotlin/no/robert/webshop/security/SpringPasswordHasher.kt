package no.robert.webshop.security

import no.robert.webshop.identity.PasswordHasher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SpringPasswordHasher(
    private val passwordEncoder: PasswordEncoder,
) : PasswordHasher {

    override fun hash(plainPassword: String): String =
        requireNotNull(passwordEncoder.encode(plainPassword)) { "Passordhash mangler" }

    override fun matches(plainPassword: String, passwordHash: String): Boolean =
        passwordEncoder.matches(plainPassword, passwordHash)
}
