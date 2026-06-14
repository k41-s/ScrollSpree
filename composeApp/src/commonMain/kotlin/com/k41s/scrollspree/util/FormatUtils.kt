package com.k41s.scrollspree.util

import com.k41s.scrollspree.domain.model.enums.PaymentMethod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

fun Double.toCurrencyDisplay(): String {
    return "$${this.toNumericString()}"
}

fun Double.toNumericString(): String {
    val totalCents = (this * 100).toLong()
    val whole = totalCents / 100
    val fraction = totalCents % 100
    return "$whole.${fraction.toString().padStart(2, '0')}"
}

fun LocalDateTime.formatToString(): String {
    val orderDateFormat = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()

        char(' ')

        hour()
        char(':')
        minute()
        char(':')
        second()
    }

    return orderDateFormat.format(this)
}

fun PaymentMethod.toDisplayName(): String {
    return when (this) {
        PaymentMethod.CARD -> "Card"
        PaymentMethod.PAYPAL -> "PayPal"
        PaymentMethod.BANK_TRANSFER -> "Bank Transfer"
        PaymentMethod.CASH -> "Cash"
    }
}