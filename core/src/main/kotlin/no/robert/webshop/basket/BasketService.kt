package no.robert.webshop.basket

import no.robert.webshop.User
import no.robert.webshop.identity.RbacService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BasketService(
    private val basketRepository: BasketRepository,
    private val basketProductRepository: BasketProductRepository,
    private val rbacService: RbacService,
) {

    @Transactional
    fun createBasket(user: User): Basket {
        rbacService.checkCustomer(user)

        if (basketRepository.findByCustomerId(user.id) != null) {
            throw BasketAlreadyExistsException(user.id)
        }

        val basket = Basket.createForCustomer(user.id)
        return basketRepository.save(basket)
    }

    @Transactional
    fun addProduct(user: User, command: AddProductToBasketCommand): Basket {
        rbacService.checkCustomer(user)

        val basket = basketRepository.findByCustomerId(user.id)
            ?: throw BasketNotFoundException(user.id)

        val product = basketProductRepository.findById(command.productId)
            ?: throw BasketProductNotFoundException(command.productId)

        val updatedBasket = basket.addProduct(product, command.quantity)
        return basketRepository.save(updatedBasket)
    }

    @Transactional
    fun removeProduct(user: User, productId: String): Basket {
        rbacService.checkCustomer(user)

        val basket = basketRepository.findByCustomerId(user.id)
            ?: throw BasketNotFoundException(user.id)

        val updatedBasket = basket.removeProduct(productId)
        return basketRepository.save(updatedBasket)
    }

    @Transactional(readOnly = true)
    fun getBasket(user: User): Basket {
        rbacService.checkCustomer(user)
        return basketRepository.findByCustomerId(user.id)
            ?: throw BasketNotFoundException(user.id)
    }
}

data class AddProductToBasketCommand(
    val productId: String,
    val quantity: Int,
)
