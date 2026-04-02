package no.robert.webshop.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        // Only process if Authorization header is present with Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            try {
                val decodedJWT = jwtUtil.validateAndDecode(token)

                if (decodedJWT != null) {
                    val userId = decodedJWT.subject
                    val email = decodedJWT.getClaim("email").asString() ?: "unknown"
                    val role = decodedJWT.getClaim("role").asString() ?: "CUSTOMER"

                    // Create UserPrincipal with user information
                    val principal = UserPrincipal(
                        id = userId,
                        email = email,
                        role = role
                    )

                    // Create authentication token with UserPrincipal
                    val authentication = UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.authorities
                    )

                    // Set in SecurityContext
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                // Invalid token - observable for support; never log token or credentials
                log.warn("JWT validation failed for request {} {}: {}", request.method, request.requestURI, e.message)
            }
        }

        // Continue filter chain (allow session-based auth if JWT not present/invalid)
        filterChain.doFilter(request, response)
    }
}
