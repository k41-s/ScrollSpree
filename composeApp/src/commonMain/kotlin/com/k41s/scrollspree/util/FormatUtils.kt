package com.k41s.scrollspree.util

fun Double.toCurrencyDisplay(): String {
    return "$${this.toNumericString()}"
}

fun Double.toNumericString(): String {
    val totalCents = (this * 100).toLong()
    val whole = totalCents / 100
    val fraction = totalCents % 100
    return "$whole.${fraction.toString().padStart(2, '0')}"
}