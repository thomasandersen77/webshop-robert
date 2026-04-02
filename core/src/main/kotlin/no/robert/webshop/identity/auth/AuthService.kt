package no.robert.webshop.identity.auth

import no.robert.webshop.User
import no.robert.webshop.UserRepository
import no.robert.webshop.UserRole
import no.robert.webshop.identity.PasswordHasher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
open class AuthService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
) {

    @Transactional
    open fun register(command: RegisterUserCommand): User {
        val normalizedEmail = command.email.trim().lowercase()
        require(normalizedEmail.isNotEmpty()) { "E-post kan ikke være tom" }
        require(command.plainPassword.length >= 8) { "Passordet må være minst 8 tegn" }

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw EmailAlreadyRegisteredException(normalizedEmail)
        }

        val hash = passwordHasher.hash(command.plainPassword)
        val user = User(
            id = UUID.randomUUID().toString(),
            email = normalizedEmail,
            passwordHash = hash,
            role = UserRole.CUSTOMER,
            active = true,
        )
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    open fun login(command: LoginCommand): User {
        val normalizedEmail = command.email.trim().lowercase()
        val user = userRepository.findByEmail(normalizedEmail)
            ?: throw InvalidCredentialsException()

        if (!user.active) {
            throw UserInactiveException()
        }

        if (!passwordHasher.matches(command.plainPassword, user.passwordHash)) {
            throw InvalidCredentialsException()
        }

        return user
    }
}
