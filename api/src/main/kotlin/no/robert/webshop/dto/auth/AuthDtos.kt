package no.robert.webshop.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/** JavaBean-stil for JSON-inn (Jackson uten kotlin-modul). */
class RegisterUserRequestDto {
    @field:NotBlank(message = "E-post er påkrevd")
    @field:Email(message = "Ugyldig e-postadresse")
    var email: String = ""

    @field:NotBlank(message = "Passord er påkrevd")
    @field:Size(min = 8, max = 128, message = "Passord må være mellom 8 og 128 tegn")
    var password: String = ""
}

class LoginRequestDto {
    @field:NotBlank(message = "E-post er påkrevd")
    @field:Email(message = "Ugyldig e-postadresse")
    var email: String = ""

    @field:NotBlank(message = "Passord er påkrevd")
    var password: String = ""
}

data class UserResponseDto(
    val id: String,
    val email: String,
    val role: String,
    val active: Boolean,
)

data class TokenResponseDto(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val user: UserResponseDto,
)

data class ErrorResponseDto(
    val message: String,
)
