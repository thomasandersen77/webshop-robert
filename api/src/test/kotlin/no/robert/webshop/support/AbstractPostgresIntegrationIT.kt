package no.robert.webshop.support

import no.robert.webshop.admin.AdminRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles

/**
 * Felles PostgreSQL via Testcontainers + [ServiceConnection] (ingen localhost:5432).
 * Underklasser kjøres av Failsafe (*IT).
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:postgresql://localhost:5432/webshop",
        "spring.datasource.username=webshop",
        "spring.datasource.password=webshop"
    ]
)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
abstract class AbstractPostgresIntegrationIT {

    @Autowired
    private lateinit var adminRepository: AdminRepository

    @BeforeEach
    fun clearDatabase() {
        adminRepository.deleteAllProducts()
        adminRepository.deleteAllCategories()
    }
}
