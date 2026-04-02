package no.robert.webshop.identity.auth

open class AuthException(message: String) : RuntimeException(message)

class EmailAlreadyRegisteredException(email: String) :
    AuthException("E-postadressen er allerede registrert: $email")

class InvalidCredentialsException : AuthException("Ugyldig e-post eller passord")

class UserInactiveException : AuthException("Brukerkontoen er deaktivert")
