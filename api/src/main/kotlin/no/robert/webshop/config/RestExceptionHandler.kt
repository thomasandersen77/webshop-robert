package no.robert.webshop.config

import no.robert.webshop.admin.DuplicateCategoryException
import no.robert.webshop.admin.ProductCategoryNotFoundException
import no.robert.webshop.basket.BasketAlreadyExistsException
import no.robert.webshop.basket.BasketNotFoundException
import no.robert.webshop.basket.BasketProductNotFoundException
import no.robert.webshop.basket.BasketProductNotInBasketException
import no.robert.webshop.dto.auth.ErrorResponseDto
import no.robert.webshop.identity.AccessDeniedException
import no.robert.webshop.identity.auth.EmailAlreadyRegisteredException
import no.robert.webshop.identity.auth.InvalidCredentialsException
import no.robert.webshop.identity.auth.UserInactiveException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(EmailAlreadyRegisteredException::class)
    fun conflict(ex: EmailAlreadyRegisteredException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto(ex.message ?: "Konflikt"))

    @ExceptionHandler(DuplicateCategoryException::class)
    fun duplicateCategory(ex: DuplicateCategoryException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto(ex.message ?: "Kategori eksisterer allerede"))

    @ExceptionHandler(ProductCategoryNotFoundException::class)
    fun productCategoryNotFound(ex: ProductCategoryNotFoundException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorResponseDto(ex.message ?: "Produktkategori ikke funnet"))

    @ExceptionHandler(BasketAlreadyExistsException::class)
    fun basketAlreadyExists(ex: BasketAlreadyExistsException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto(ex.message ?: "Handlekurv finnes allerede"))

    @ExceptionHandler(BasketNotFoundException::class)
    fun basketNotFound(ex: BasketNotFoundException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto(ex.message ?: "Handlekurv ikke funnet"))

    @ExceptionHandler(BasketProductNotFoundException::class)
    fun basketProductNotFound(ex: BasketProductNotFoundException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto(ex.message ?: "Produkt ikke funnet"))

    @ExceptionHandler(BasketProductNotInBasketException::class)
    fun basketProductNotInBasket(ex: BasketProductNotInBasketException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto(ex.message ?: "Produkt finnes ikke i handlekurven"))

    @ExceptionHandler(InvalidCredentialsException::class)
    fun unauthorized(ex: InvalidCredentialsException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponseDto(ex.message ?: "Ikke autorisert"))

    @ExceptionHandler(UserInactiveException::class)
    fun forbidden(ex: UserInactiveException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponseDto(ex.message ?: "Forbudt"))

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDenied(ex: AccessDeniedException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponseDto(ex.message ?: "Ingen tilgang"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        val msg = ex.bindingResult.fieldErrors.joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto(msg))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto(ex.message ?: "Ugyldig forespørsel"))
}
