package no.robert.webshop.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import no.robert.webshop.basket.BasketRepository
import no.robert.webshop.support.AbstractPostgresIntegrationIT
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CustomerIntegrationIT : AbstractPostgresIntegrationIT() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var basketRepository: BasketRepository

    @Test
    fun `can register customer and it gets a basket`() {
        val email = "customer-${System.nanoTime()}@example.com"
        val password = "password123"

        val responseJson = mockMvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$email","password":"$password"}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.user.email").value(email))
            .andExpect(jsonPath("$.user.role").value("CUSTOMER"))
            .andReturn()
            .response
            .contentAsString

        val userId = objectMapper.readTree(responseJson).get("user").get("id").asText()

        // Verify basket was created
        val basket = basketRepository.findByCustomerId(userId)
        assert(basket != null) { "Basket should have been created for customer" }
        assert(basket?.customerId == userId)
        assert(basket?.items?.isEmpty() == true)
    }

    @Test
    fun `cannot register with same email twice`() {
        val email = "duplicate-${System.nanoTime()}@example.com"
        val password = "password123"

        mockMvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$email","password":"$password"}"""),
        ).andExpect(status().isCreated)

        mockMvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$email","password":"$password"}"""),
        ).andExpect(status().isConflict) // AuthService throws EmailAlreadyRegisteredException which should map to 409
    }
}
