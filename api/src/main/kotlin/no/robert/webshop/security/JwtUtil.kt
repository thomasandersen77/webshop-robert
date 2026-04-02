package no.robert.webshop.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {

    @Value("\${jwt.secret:default-secret-key-change-in-production-min-32}")
    private lateinit var secret: String

    @Value("\${jwt.expiration:86400000}")
    private var expiration: Long = 86400000

    private val algorithm: Algorithm by lazy { Algorithm.HMAC256(secret) }

    @PostConstruct
    fun validateSecret() {
        require(secret.length >= 32) {
            "jwt.secret må være minst 32 tegn (nå: ${secret.length}). Sett jwt.secret i konfigurasjon."
        }
        Algorithm.HMAC256(secret)
    }

    fun generateToken(userId: String, email: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)
        return JWT.create()
            .withSubject(userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .sign(algorithm)
    }

    fun validateAndDecode(token: String): DecodedJWT? {
        return try {
            JWT.require(algorithm).build().verify(token)
        } catch (_: Exception) {
            null
        }
    }

    fun getUserIdFromToken(token: String): String? =
        validateAndDecode(token)?.subject
}
