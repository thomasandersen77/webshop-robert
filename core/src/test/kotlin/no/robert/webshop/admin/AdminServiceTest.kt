package no.robert.webshop.admin

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.robert.webshop.Product
import no.robert.webshop.ProductCategory
import no.robert.webshop.User
import no.robert.webshop.UserRole
import no.robert.webshop.identity.AccessDeniedException
import no.robert.webshop.identity.RbacService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AdminServiceTest {

    private val repository = mockk<AdminRepository>()
    private val rbacService = RbacService()
    private val adminService = AdminService(repository, rbacService)

    private val adminUser = User("1", "admin@webshop.no", "HASH", UserRole.ADMIN)
    private val regularUser = User("2", "user@webshop.no", "HASH", UserRole.CUSTOMER)

    @Test
    fun `admin can create valid product category`() {
        val categoryName = "Propane Bottles"
        every { repository.findCategoryByName(categoryName) } returns null
        every { repository.saveCategory(any()) } answers { it.invocation.args[0] as ProductCategory }

        val result = adminService.createProductCategory(adminUser, categoryName)

        assertEquals(categoryName, result.name)
        verify { repository.saveCategory(match { it.name == categoryName }) }
    }

    @Test
    fun `non-admin cannot create product category`() {
        assertThrows(AccessDeniedException::class.java) {
            adminService.createProductCategory(regularUser, "Any category")
        }
    }

    @Test
    fun `cannot create category with blank name`() {
        assertThrows(IllegalArgumentException::class.java) {
            adminService.createProductCategory(adminUser, "   ")
        }
    }

    @Test
    fun `cannot create duplicate category`() {
        val categoryName = "Existing"
        every { repository.findCategoryByName(categoryName) } returns ProductCategory(id = "123", name = categoryName)

        assertThrows(DuplicateCategoryException::class.java) {
            adminService.createProductCategory(adminUser, categoryName)
        }
    }

    @Test
    fun `admin can create valid product`() {
        val command = CreateProductCommand("cat1", "Name", "Desc", 100, "NOK", 5)
        every { repository.findCategoryById("cat1") } returns ProductCategory("cat1", "Cat")
        every { repository.saveProduct(any()) } answers { it.invocation.args[0] as Product }

        val result = adminService.createProduct(adminUser, command)

        assertEquals("Name", result.name)
        assertEquals("Desc", result.description)
        assertEquals(100L, result.price.amountMinor)
        assertEquals("NOK", result.price.currency)
        assertEquals(5, result.ratingStars)
        verify { repository.saveProduct(any()) }
    }

    @Test
    fun `cannot create product in non-existing category`() {
        val command = CreateProductCommand("non-existing", "Name", "Desc", 100, "NOK", 5)
        every { repository.findCategoryById("non-existing") } returns null

        assertThrows(ProductCategoryNotFoundException::class.java) {
            adminService.createProduct(adminUser, command)
        }
    }

    @Test
    fun `non-admin cannot create product`() {
        val command = CreateProductCommand("cat1", "Name", "Desc", 100, "NOK", 5)
        assertThrows(AccessDeniedException::class.java) {
            adminService.createProduct(regularUser, command)
        }
    }

    @Test
    fun `validate product name`() {
        val command = CreateProductCommand("cat1", " ", "Desc", 100, "NOK", 5)
        assertThrows(IllegalArgumentException::class.java) {
            adminService.createProduct(adminUser, command)
        }
    }

    @Test
    fun `validate product price`() {
        val command = CreateProductCommand("cat1", "Name", "Desc", -1, "NOK", 5)
        assertThrows(IllegalArgumentException::class.java) {
            adminService.createProduct(adminUser, command)
        }
    }

    @Test
    fun `validate product rating`() {
        assertThrows(IllegalArgumentException::class.java) {
            adminService.createProduct(adminUser, CreateProductCommand("cat1", "N", "D", 1, "NOK", 0))
        }
        assertThrows(IllegalArgumentException::class.java) {
            adminService.createProduct(adminUser, CreateProductCommand("cat1", "N", "D", 1, "NOK", 6))
        }
    }
}
