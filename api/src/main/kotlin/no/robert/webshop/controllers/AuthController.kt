package no.robert.webshop.controllers

import jakarta.validation.Valid
import no.robert.webshop.User
import no.robert.webshop.dto.auth.ErrorResponseDto
import no.robert.webshop.dto.auth.LoginRequestDto
import no.robert.webshop.dto.auth.RegisterUserRequestDto
import no.robert.webshop.dto.auth.TokenResponseDto
import no.robert.webshop.dto.auth.toLoginCommand
import no.robert.webshop.dto.auth.toRegisterCommand
import no.robert.webshop.dto.auth.toUserResponseDto
import no.robert.webshop.identity.auth.AuthService
import no.robert.webshop.security.CurrentUser
import no.robert.webshop.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtUtil: JwtUtil,
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody body: RegisterUserRequestDto): TokenResponseDto {
        val user = authService.register(body.toRegisterCommand())
        val token = jwtUtil.generateToken(user.id, user.email, user.role.name)
        return TokenResponseDto(accessToken = token, user = user.toUserResponseDto())
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody body: LoginRequestDto): TokenResponseDto {
        val user = authService.login(body.toLoginCommand())
        val token = jwtUtil.generateToken(user.id, user.email, user.role.name)
        return TokenResponseDto(accessToken = token, user = user.toUserResponseDto())
    }

    @GetMapping("/me")
    fun me(@CurrentUser user: User): ResponseEntity<Any> {
        if (!user.active) {
            return ResponseEntity.status(403).body(ErrorResponseDto("Brukerkontoen er deaktivert"))
        }
        return ResponseEntity.ok(user.toUserResponseDto())
    }
}
