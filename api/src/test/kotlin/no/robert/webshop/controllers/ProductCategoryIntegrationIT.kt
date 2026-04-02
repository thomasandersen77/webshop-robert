package no.robert.webshop.controllers

import no.robert.webshop.User
import no.robert.webshop.UserRepository
import no.robert.webshop.UserRole
import no.robert.webshop.admin.AdminService
import no.robert.webshop.admin.CreateProductCommand
import no.robert.webshop.support.AbstractPostgresIntegrationIT
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class ProductCategoryIntegrationIT : AbstractPostgresIntegrationIT() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var adminService: AdminService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `anyone can get all categories with products`() {
        // Setup: Create a user and some data using AdminService
        val admin = userRepository.save(User(UUID.randomUUID().toString(), "admin@webshop.no", "hash", UserRole.ADMIN))
        
        val category = adminService.createProductCategory(admin, "Propane")
        adminService.createProduct(admin, CreateProductCommand(category.id!!, "11kg Cylinder", "Desc", 29900, 5))
        adminService.createProduct(admin, CreateProductCommand(category.id!!, "5kg Cylinder", "Desc", 19900, 4))
        
        val otherCategory = adminService.createProductCategory(admin, "Accessories")

        // Act & Assert
        mockMvc.perform(get("/api/product-categories"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[?(@.name == 'Propane')].products.length()").value(2))
            .andExpect(jsonPath("$[?(@.name == 'Propane')].products[?(@.name == '11kg Cylinder')]").exists())
            .andExpect(jsonPath("$[?(@.name == 'Accessories')].products.length()").value(0))
    }

    @Test
    fun `anyone can get one category with its products`() {
        // Setup
        val admin = userRepository.save(User(UUID.randomUUID().toString(), "admin2@webshop.no", "hash", UserRole.ADMIN))
        val category = adminService.createProductCategory(admin, "BBQ")
        adminService.createProduct(admin, CreateProductCommand(category.id!!, "Gas Grill", "Shiny", 599000, 5))

        // Act & Assert
        mockMvc.perform(get("/api/product-categories/${category.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("BBQ"))
            .andExpect(jsonPath("$.products.length()").value(1))
            .andExpect(jsonPath("$.products[0].name").value("Gas Grill"))
    }

    @Test
    fun `returns 404 for non-existing category`() {
        mockMvc.perform(get("/api/product-categories/non-existing-id"))
            .andExpect(status().isNotFound)
    }
}
