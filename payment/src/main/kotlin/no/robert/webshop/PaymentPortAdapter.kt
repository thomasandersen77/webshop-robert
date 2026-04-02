package no.robert.webshop

import no.robert.webshop.payment.PaymentPort

class PaymentPortAdapter: PaymentPort {
    override fun processPayment(order: OrderSummary): Boolean {
        TODO("Not yet implemented")
    }
}