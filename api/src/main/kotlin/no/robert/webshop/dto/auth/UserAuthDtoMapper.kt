package no.robert.webshop.dto.auth

import no.robert.webshop.User
import no.robert.webshop.identity.auth.LoginCommand
import no.robert.webshop.identity.auth.RegisterUserCommand

fun RegisterUserRequestDto.toRegisterCommand(): RegisterUserCommand =
    RegisterUserCommand(email = this.email, plainPassword = this.password)

fun LoginRequestDto.toLoginCommand(): LoginCommand =
    LoginCommand(email = this.email, plainPassword = this.password)

fun User.toUserResponseDto(): UserResponseDto =
    UserResponseDto(
        id = id,
        email = email,
        role = role.name,
        active = active,
    )
