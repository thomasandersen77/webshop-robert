package no.robert.webshop.identity.auth

data class LoginCommand(
    val email: String,
    val plainPassword: String,
)
