package no.robert.webshop.identity.auth

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import no.robert.webshop.User
import no.robert.webshop.UserRepository
import no.robert.webshop.UserRole
import no.robert.webshop.identity.PasswordHasher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AuthServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val authService = AuthService(userRepository, passwordHasher)

    @Test
    fun `register creates CUSTOMER with hashed password`() {
        every { userRepository.existsByEmail("a@b.no") } returns false
        every { passwordHasher.hash("password123") } returns "HASH"
        val saved = slot<User>()
        every { userRepository.save(capture(saved)) } answers { saved.captured }

        val user = authService.register(RegisterUserCommand("A@B.no", "password123"))

        assertEquals("a@b.no", user.email)
        assertEquals(UserRole.CUSTOMER, user.role)
        assertEquals("HASH", user.passwordHash)
        assertEquals(true, user.active)
        assertEquals("a@b.no", saved.captured.email)
        assertEquals("HASH", saved.captured.passwordHash)
        verify(exactly = 1) { userRepository.save(saved.captured) }
    }

    @Test
    fun `register rejects duplicate email`() {
        every { userRepository.existsByEmail("x@y.no") } returns true

        assertThrows(EmailAlreadyRegisteredException::class.java) {
            authService.register(RegisterUserCommand("x@y.no", "password123"))
        }
    }

    @Test
    fun `login returns user when credentials match`() {
        val stored = User("1", "a@b.no", "HASH", UserRole.CUSTOMER, true)
        every { userRepository.findByEmail("a@b.no") } returns stored
        every { passwordHasher.matches("secret1234", "HASH") } returns true

        val user = authService.login(LoginCommand("a@b.no", "secret1234"))

        assertEquals(stored, user)
    }

    @Test
    fun `login rejects wrong password`() {
        val stored = User("1", "a@b.no", "HASH", UserRole.CUSTOMER, true)
        every { userRepository.findByEmail("a@b.no") } returns stored
        every { passwordHasher.matches("wrong", "HASH") } returns false

        assertThrows(InvalidCredentialsException::class.java) {
            authService.login(LoginCommand("a@b.no", "wrong"))
        }
    }

    @Test
    fun `login rejects inactive user`() {
        val stored = User("1", "a@b.no", "HASH", UserRole.CUSTOMER, active = false)
        every { userRepository.findByEmail("a@b.no") } returns stored

        assertThrows(UserInactiveException::class.java) {
            authService.login(LoginCommand("a@b.no", "secret1234"))
        }
    }
}
