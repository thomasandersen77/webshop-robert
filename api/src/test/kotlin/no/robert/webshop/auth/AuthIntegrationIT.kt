package no.robert.webshop.auth

import com.fasterxml.jackson.databind.ObjectMapper
import no.robert.webshop.support.AbstractPostgresIntegrationIT
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthIntegrationIT : AbstractPostgresIntegrationIT() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `register login and me with bearer token`() {
        val email = "integration-${System.nanoTime()}@example.com"
        val password = "password123"

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$email","password":"$password"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.user.email").value(email.lowercase()))

        val loginJson = mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$email","password":"$password"}"""),
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val token = objectMapper.readTree(loginJson).get("accessToken").asText()

        mockMvc.perform(
            get("/api/auth/me").header("Authorization", "Bearer $token"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value(email.lowercase()))
    }

    @Test
    fun `me without token returns forbidden`() {
        mockMvc.perform(get("/api/auth/me"))
            .andExpect(status().isForbidden)
    }
}
