package no.robert.webshop.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.robert.webshop.Money
import no.robert.webshop.User
import no.robert.webshop.UserRole
import no.robert.webshop.basket.AddProductToBasketCommand
import no.robert.webshop.basket.Basket
import no.robert.webshop.basket.BasketItem
import no.robert.webshop.basket.BasketProductNotInBasketException
import no.robert.webshop.basket.BasketService
import no.robert.webshop.config.RestExceptionHandler
import no.robert.webshop.security.CurrentUser
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.core.MethodParameter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

class CartControllerTest {

    private val basketService = mockk<BasketService>()
    private val objectMapper: ObjectMapper = Jackson2ObjectMapperBuilder.json().build()
    private lateinit var mockMvc: MockMvc

    private val customer = User(
        id = "customer-1",
        email = "customer@example.com",
        passwordHash = "hash",
        role = UserRole.CUSTOMER,
    )

    @BeforeEach
    fun setUp() {
        clearMocks(basketService)

        val validator = LocalValidatorFactoryBean()
        validator.afterPropertiesSet()

        mockMvc = MockMvcBuilders
            .standaloneSetup(CartController(basketService))
            .setControllerAdvice(RestExceptionHandler())
            .setCustomArgumentResolvers(StaticCurrentUserArgumentResolver(customer))
            .setMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
            .setValidator(validator)
            .build()
    }

    @Test
    fun `create basket returns 201 and cart body`() {
        val basket = sampleBasket(quantity = 2)
        every { basketService.createBasket(customer) } returns basket

        mockMvc.perform(post("/api/cart"))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.basketId").value("basket-1"))
            .andExpect(jsonPath("$.customerId").value("customer-1"))
            .andExpect(jsonPath("$.items.length()").value(1))
            .andExpect(jsonPath("$.total.amountMinor").value(59800))
            .andExpect(jsonPath("$.total.currency").value("NOK"))

        verify(exactly = 1) { basketService.createBasket(customer) }
    }

    @Test
    fun `add item returns updated cart`() {
        val basket = sampleBasket(quantity = 3)
        every {
            basketService.addProduct(customer, AddProductToBasketCommand("product-1", 3))
        } returns basket

        mockMvc.perform(
            post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"productId":"product-1","quantity":3}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[0].productId").value("product-1"))
            .andExpect(jsonPath("$.items[0].quantity").value(3))
            .andExpect(jsonPath("$.items[0].lineTotal.amountMinor").value(89700))

        verify(exactly = 1) {
            basketService.addProduct(customer, AddProductToBasketCommand("product-1", 3))
        }
    }

    @Test
    fun `add item validates quantity`() {
        mockMvc.perform(
            post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"productId":"product-1","quantity":0}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message", containsString("quantity: quantity må være minst 1")))
    }

    @Test
    fun `remove unknown product returns 404`() {
        every { basketService.removeProduct(customer, "product-x") } throws BasketProductNotInBasketException("product-x")

        mockMvc.perform(delete("/api/cart/items/product-x"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message", containsString("product-x")))
    }

    @Test
    fun `get cart returns cart body`() {
        val basket = sampleBasket(quantity = 1)
        every { basketService.getBasket(customer) } returns basket

        mockMvc.perform(get("/api/cart"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.basketId").value("basket-1"))
            .andExpect(jsonPath("$.items[0].unitPrice.amountMinor").value(29900))
            .andExpect(jsonPath("$.total.amountMinor").value(29900))
    }

    private fun sampleBasket(quantity: Int): Basket {
        return Basket(
            id = "basket-1",
            customerId = "customer-1",
            items = listOf(
                BasketItem(
                    productId = "product-1",
                    quantity = quantity,
                    unitPrice = Money.nok(29900),
                ),
            ),
        )
    }
}

private class StaticCurrentUserArgumentResolver(
    private val user: User,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUser::class.java) &&
            User::class.java.isAssignableFrom(parameter.parameterType)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        return user
    }
}
