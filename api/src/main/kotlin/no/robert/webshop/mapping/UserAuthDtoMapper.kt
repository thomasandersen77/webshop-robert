package no.robert.webshop.mapping

import no.robert.webshop.User
import no.robert.webshop.controllers.dto.LoginRequestDto
import no.robert.webshop.controllers.dto.RegisterUserRequestDto
import no.robert.webshop.controllers.dto.UserResponseDto
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
