package no.robert.webshop.identity.auth

data class RegisterUserCommand(
    val email: String,
    val plainPassword: String,
)
