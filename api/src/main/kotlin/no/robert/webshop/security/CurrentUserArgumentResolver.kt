package no.robert.webshop.security

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import no.robert.webshop.User
import no.robert.webshop.UserRepository

/**
 * Resolver som injiserer den innloggede User-entiteten i controller-metoder
 * annotert med @CurrentUser.
 * 
 * Henter bruker-ID fra SecurityContext (UserPrincipal) og slår opp
 * den faktiske User-entiteten fra databasen.
 */
@Component
class CurrentUserArgumentResolver(
    private val userRepository: UserRepository
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUser::class.java) &&
                User::class.java.isAssignableFrom(parameter.parameterType)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): User {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("Ingen autentisering funnet i sikkerhetskonteksten")

        if (!authentication.isAuthenticated || authentication.principal == "anonymousUser") {
            throw IllegalStateException("Bruker er ikke autentisert")
        }

        val principal = authentication.principal as? UserPrincipal
            ?: throw IllegalStateException("Ugyldig principal-type: ${authentication.principal?.javaClass}")

        return userRepository.findById(principal.id)
            ?: throw IllegalStateException("Bruker ikke funnet: ${principal.id}")
    }
}
