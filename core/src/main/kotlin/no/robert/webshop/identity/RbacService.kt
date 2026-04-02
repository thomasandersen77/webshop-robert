package no.robert.webshop.identity

import no.robert.webshop.User
import no.robert.webshop.UserRole
import org.springframework.stereotype.Service

/**
 * RBAC (Role-Based Access Control) Service
 */
@Service
class RbacService {

    fun isAdmin(user: User): Boolean {
        return user.role == UserRole.ADMIN
    }

    fun checkAdmin(user: User) {
        if (!isAdmin(user)) {
            throw AccessDeniedException("User ${user.id} does not have ADMIN role")
        }
    }
}

class AccessDeniedException(message: String) : RuntimeException(message)
