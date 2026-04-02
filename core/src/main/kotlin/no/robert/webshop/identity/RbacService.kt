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

    fun isCustomer(user: User): Boolean {
        return user.role == UserRole.CUSTOMER
    }

    fun checkAdmin(user: User) {
        if (!isAdmin(user)) {
            throw AccessDeniedException("User ${user.id} does not have ADMIN role")
        }
    }

    fun checkCustomer(user: User) {
        if (!isCustomer(user)) {
            throw AccessDeniedException("Bruker ${user.id} har ikke CUSTOMER-rolle")
        }
    }
}

class AccessDeniedException(message: String) : RuntimeException(message)
