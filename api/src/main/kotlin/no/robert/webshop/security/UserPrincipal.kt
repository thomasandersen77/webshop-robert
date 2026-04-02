package no.robert.webshop.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * UserPrincipal representerer den autentiserte brukeren i SecurityContext.
 * Brukes med @AuthenticationPrincipal i controller-metoder.
 */
data class UserPrincipal(
    val id: String,
    val email: String,
    val role: String
) : UserDetails {
    
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_$role"))
    }
    
    override fun getPassword(): String? = null
    
    override fun getUsername(): String = email
    
    override fun isAccountNonExpired(): Boolean = true
    
    override fun isAccountNonLocked(): Boolean = true
    
    override fun isCredentialsNonExpired(): Boolean = true
    
    override fun isEnabled(): Boolean = true
}
