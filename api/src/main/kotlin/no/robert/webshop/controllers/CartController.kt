package no.robert.webshop.controllers

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import no.robert.webshop.User
import no.robert.webshop.basket.AddProductToBasketCommand
import no.robert.webshop.basket.Basket
import no.robert.webshop.basket.BasketItem
import no.robert.webshop.basket.BasketService
import no.robert.webshop.security.CurrentUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val basketService: BasketService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBasket(@CurrentUser user: User): CartResponseDto {
        val basket = basketService.createBasket(user)
        return CartResponseDto.from(basket)
    }

    @PostMapping("/items")
    fun addItem(
        @CurrentUser user: User,
        @Valid @RequestBody request: AddCartItemRequestDto,
    ): CartResponseDto {
        val basket = basketService.addProduct(user, request.toCommand())
        return CartResponseDto.from(basket)
    }

    @DeleteMapping("/items/{productId}")
    fun removeItem(
        @CurrentUser user: User,
        @PathVariable productId: String,
    ): CartResponseDto {
        val basket = basketService.removeProduct(user, productId)
        return CartResponseDto.from(basket)
    }

    @GetMapping
    fun getBasket(@CurrentUser user: User): CartResponseDto {
        val basket = basketService.getBasket(user)
        return CartResponseDto.from(basket)
    }
}

class AddCartItemRequestDto {
    @field:NotBlank(message = "productId er påkrevd")
    var productId: String = ""

    @field:Min(value = 1, message = "quantity må være minst 1")
    var quantity: Int = 0

    fun toCommand(): AddProductToBasketCommand {
        return AddProductToBasketCommand(
            productId = productId,
            quantity = quantity,
        )
    }
}

data class CartResponseDto(
    val basketId: String,
    val customerId: String,
    val items: List<CartItemResponseDto>,
    val total: MoneyResponseDto,
) {
    companion object {
        fun from(domain: Basket): CartResponseDto {
            return CartResponseDto(
                basketId = domain.id,
                customerId = domain.customerId,
                items = domain.items.map { CartItemResponseDto.from(it) },
                total = MoneyResponseDto(domain.totalAmountMinor()),
            )
        }
    }
}

data class CartItemResponseDto(
    val productId: String,
    val quantity: Int,
    val unitPrice: MoneyResponseDto,
    val lineTotal: MoneyResponseDto,
) {
    companion object {
        fun from(domain: BasketItem): CartItemResponseDto {
            return CartItemResponseDto(
                productId = domain.productId,
                quantity = domain.quantity,
                unitPrice = MoneyResponseDto(domain.unitPriceMinor),
                lineTotal = MoneyResponseDto(domain.lineAmountMinor()),
            )
        }
    }
}

data class MoneyResponseDto(
    val amountMinor: Long,
    val currency: String = "NOK",
)
