package no.robert.webshop.identity.customer

import no.robert.webshop.User
import no.robert.webshop.basket.BasketService
import no.robert.webshop.identity.auth.AuthService
import no.robert.webshop.identity.auth.RegisterUserCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerService(
    private val authService: AuthService,
    private val basketService: BasketService,
) {

    @Transactional
    fun createCustomer(command: CreateCustomerCommand): User {
        val user = authService.register(
            RegisterUserCommand(
                email = command.email,
                plainPassword = command.password
            )
        )
        basketService.createBasket(user)
        return user
    }
}

data class CreateCustomerCommand(
    val email: String,
    val password: String,
)
