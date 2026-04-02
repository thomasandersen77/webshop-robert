package no.robert.webshop.admin

import com.fasterxml.jackson.databind.ObjectMapper
import no.robert.webshop.User
import no.robert.webshop.UserRepository
import no.robert.webshop.UserRole
import no.robert.webshop.controllers.CreateProductRequest
import no.robert.webshop.security.JwtUtil
import no.robert.webshop.support.AbstractPostgresIntegrationIT
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AdminIntegrationIT : AbstractPostgresIntegrationIT() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Test
    fun `admin can create product category`() {
        val adminEmail = "admin-${System.nanoTime()}@example.com"
        val password = "password123"

        val registerJson = mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$adminEmail","password":"$password"}"""),
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val userId = objectMapper.readTree(registerJson).get("user").get("id").asText()

        val user = userRepository.findById(userId)!!
        val adminUser = userRepository.save(user.copy(role = UserRole.ADMIN))

        val token = jwtUtil.generateToken(adminUser.id, adminUser.email, adminUser.role.name)

        val categoryName = "Propane Bottles"
        mockMvc.perform(
            post("/api/admin/product-categories")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"$categoryName"}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(categoryName))

        mockMvc.perform(
            post("/api/admin/product-categories")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"$categoryName"}"""),
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun `non-admin cannot create product category`() {
        val userEmail = "user-${System.nanoTime()}@example.com"
        val password = "password123"

        val registerJson = mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$userEmail","password":"$password"}"""),
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val token = objectMapper.readTree(registerJson).get("accessToken").asText()

        mockMvc.perform(
            post("/api/admin/product-categories")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Some Category"}"""),
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `anonymous cannot create product category`() {
        mockMvc.perform(
            post("/api/admin/product-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Some Category"}"""),
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `admin can create valid product`() {
        val adminEmail = "admin-prod-${System.nanoTime()}@example.com"
        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$adminEmail","password":"password123"}"""),
        ).andExpect(status().isOk)

        val adminUser = userRepository.save(userRepository.findByEmail(adminEmail)!!.copy(role = UserRole.ADMIN))
        val token = jwtUtil.generateToken(adminUser.id, adminUser.email, adminUser.role.name)

        // 1. Create category
        val categoryJson = mockMvc.perform(
            post("/api/admin/product-categories")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Propane"}"""),
        ).andExpect(status().isCreated).andReturn().response.contentAsString
        val categoryId = objectMapper.readTree(categoryJson).get("id").asText()

        // 2. Create product
        val request = CreateProductRequest(
            categoryId = categoryId,
            name = "11kg Cylinder",
            description = "Steel cylinder",
            priceMinor = 29900,
            ratingStars = 5
        )

        mockMvc.perform(
            post("/api/admin/products")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("11kg Cylinder"))
            .andExpect(jsonPath("$.categoryId").value(categoryId))
    }

    @Test
    fun `cannot create product in non-existing category`() {
        val adminEmail = "admin-prod-err-${System.nanoTime()}@example.com"
        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"$adminEmail","password":"password123"}"""),
        ).andExpect(status().isOk)

        val adminUser = userRepository.save(userRepository.findByEmail(adminEmail)!!.copy(role = UserRole.ADMIN))
        val token = jwtUtil.generateToken(adminUser.id, adminUser.email, adminUser.role.name)

        val request = CreateProductRequest(
            categoryId = "non-existing-id",
            name = "Name",
            description = "Desc",
            priceMinor = 100,
            ratingStars = 5
        )

        mockMvc.perform(
            post("/api/admin/products")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isUnprocessableEntity)
    }
}
