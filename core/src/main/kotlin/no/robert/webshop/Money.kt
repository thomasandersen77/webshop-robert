package no.robert.webshop

data class Money(
    val amountMinor: Long,
    val currency: String = "NOK"
) {
    companion object {
        val SUPPORTED_CURRENCIES = setOf("NOK", "USD", "EUR")

        fun nok(amountMinor: Long) = Money(amountMinor, "NOK")
        fun usd(amountMinor: Long) = Money(amountMinor, "USD")
        fun eur(amountMinor: Long) = Money(amountMinor, "EUR")

        fun zero(currency: String = "NOK") = Money(0, currency)
    }

    init {
        require(currency.uppercase() in SUPPORTED_CURRENCIES) {
            "Valuta $currency støttes ikke. Støttede valutaer er: $SUPPORTED_CURRENCIES"
        }
    }

    operator fun plus(other: Money): Money {
        require(this.currency == other.currency) {
            "Kan ikke summere forskjellige valutaer: ${this.currency} og ${other.currency}"
        }
        return Money(this.amountMinor + other.amountMinor, this.currency)
    }

    operator fun times(quantity: Int): Money {
        return Money(this.amountMinor * quantity, this.currency)
    }

    override fun toString(): String {
        return "${amountMinor / 100}.${(amountMinor % 100).toString().padStart(2, '0')} $currency"
    }
}

fun Iterable<Money>.sumOf(currency: String): Money {
    return this.fold(Money.zero(currency)) { acc, money -> acc + money }
}
