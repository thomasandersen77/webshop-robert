package no.robert.webshop.controllers

import jakarta.validation.Valid
import no.robert.webshop.controllers.dto.RegisterUserRequestDto
import no.robert.webshop.controllers.dto.TokenResponseDto
import no.robert.webshop.identity.customer.CreateCustomerCommand
import no.robert.webshop.identity.customer.CustomerService
import no.robert.webshop.mapping.toUserResponseDto
import no.robert.webshop.security.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService,
    private val jwtUtil: JwtUtil,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@Valid @RequestBody request: RegisterUserRequestDto): TokenResponseDto {
        val user = customerService.createCustomer(
            CreateCustomerCommand(
                email = request.email,
                password = request.password
            )
        )
        val token = jwtUtil.generateToken(user.id, user.email, user.role.name)
        return TokenResponseDto(
            accessToken = token,
            user = user.toUserResponseDto()
        )
    }
}
