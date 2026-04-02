package no.robert.webshop.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SessionAuthenticationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Only process if no authentication exists yet (JWT takes precedence)
        val existingAuth = SecurityContextHolder.getContext().authentication
        if (existingAuth == null || !existingAuth.isAuthenticated || existingAuth.principal == "anonymousUser") {
            // Check for session-based authentication
            val session = request.getSession(false)
            if (session != null) {
                val userId = when (val rawUserId = session.getAttribute("userId")) {
                    is String -> rawUserId
                    is Long -> rawUserId.toString()
                    is Int -> rawUserId.toString()
                    else -> null
                }
                val roleName = when (val rawRole = session.getAttribute("userRole")) {
                    is Enum<*> -> rawRole.name
                    is String -> rawRole
                    else -> null
                }?.takeIf { it.isNotBlank() }?.removePrefix("ROLE_")
                val userEmail = (session.getAttribute("userEmail") as? String)?.takeIf { it.isNotBlank() }
                    ?: "session-user-$userId"

                if (userId != null && roleName != null) {
                    val principal = UserPrincipal(
                        id = userId,
                        email = userEmail,
                        role = roleName
                    )

                    val authentication = UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.authorities
                    )

                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}
