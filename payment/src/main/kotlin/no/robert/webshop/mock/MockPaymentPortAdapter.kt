package no.robert.webshop.mock

import no.robert.webshop.OrderSummary
import no.robert.webshop.payment.PaymentPort
import org.springframework.stereotype.Service


@Service
class MockPaymentPortAdapter: PaymentPort {
    override fun processPayment(order: OrderSummary): Boolean {
        return true
    }
}