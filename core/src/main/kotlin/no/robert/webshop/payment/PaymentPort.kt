package no.robert.webshop.payment

import no.robert.webshop.OrderSummary

interface PaymentPort {
    fun processPayment(order: OrderSummary): Boolean

}