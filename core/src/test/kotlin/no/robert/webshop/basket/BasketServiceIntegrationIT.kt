package no.robert.webshop.basket

import no.robert.webshop.User
import no.robert.webshop.UserRepository
import no.robert.webshop.UserRole
import no.robert.webshop.admin.AdminService
import no.robert.webshop.admin.CreateProductCommand
import no.robert.webshop.identity.PasswordHasher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.context.annotation.Bean
import java.util.UUID

@SpringBootTest(
    classes = [
        BasketServiceIntegrationIT.TestApplication::class,
        BasketServiceIntegrationIT.PasswordHasherTestConfiguration::class,
    ],
    properties = [
        "spring.datasource.url=jdbc:h2:mem:basket-it;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false",
        "spring.liquibase.enabled=false",
    ],
)
class BasketServiceIntegrationIT {

    @Autowired
    private lateinit var basketService: BasketService

    @Autowired
    private lateinit var basketRepository: BasketRepository

    @Autowired
    private lateinit var adminService: AdminService

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var customer: User
    private lateinit var productId: String

    @BeforeEach
    fun setUp() {
        basketRepository.deleteAll()
        userRepository.deleteAll()

        val admin = userRepository.save(
            User(
                id = UUID.randomUUID().toString(),
                email = "admin-${System.nanoTime()}@example.com",
                passwordHash = "hash",
                role = UserRole.ADMIN,
            ),
        )

        customer = userRepository.save(
            User(
                id = UUID.randomUUID().toString(),
                email = "customer-${System.nanoTime()}@example.com",
                passwordHash = "hash",
                role = UserRole.CUSTOMER,
            ),
        )

        val category = adminService.createProductCategory(admin, "Gass")
        val product = adminService.createProduct(
            admin,
            CreateProductCommand(
                categoryId = category.id!!,
                name = "11kg Flaske",
                description = "Staalflaske",
                priceMinor = 29900,
                ratingStars = 5,
            ),
        )
        productId = product.id!!
    }

    @Test
    fun `basket flow persists add remove and total`() {
        val created = basketService.createBasket(customer)
        assertEquals(customer.id, created.customerId)
        assertEquals(0, created.items.size)

        val afterFirstAdd = basketService.addProduct(customer, AddProductToBasketCommand(productId, 2))
        assertEquals(1, afterFirstAdd.items.size)
        assertEquals(2, afterFirstAdd.items.first().quantity)
        assertEquals(59800L, afterFirstAdd.totalAmountMinor())

        val afterSecondAdd = basketService.addProduct(customer, AddProductToBasketCommand(productId, 1))
        assertEquals(1, afterSecondAdd.items.size)
        assertEquals(3, afterSecondAdd.items.first().quantity)
        assertEquals(89700L, afterSecondAdd.totalAmountMinor())

        val persisted = basketRepository.findByCustomerId(customer.id)
        assertEquals(1, persisted?.items?.size)
        assertEquals(3, persisted?.items?.first()?.quantity)
        assertEquals(89700L, persisted?.totalAmountMinor())

        val afterRemove = basketService.removeProduct(customer, productId)
        assertEquals(0, afterRemove.items.size)
        assertEquals(0L, afterRemove.totalAmountMinor())
    }

    @SpringBootApplication(scanBasePackages = ["no.robert.webshop"])
    @EnableJpaRepositories(basePackages = ["no.robert.webshop"])
    @EntityScan(basePackages = ["no.robert.webshop"])
    open class TestApplication

    @TestConfiguration
    class PasswordHasherTestConfiguration {
        @Bean
        fun passwordHasher(): PasswordHasher {
            return object : PasswordHasher {
                override fun hash(plainPassword: String): String = plainPassword
                override fun matches(plainPassword: String, passwordHash: String): Boolean = plainPassword == passwordHash
            }
        }
    }
}
