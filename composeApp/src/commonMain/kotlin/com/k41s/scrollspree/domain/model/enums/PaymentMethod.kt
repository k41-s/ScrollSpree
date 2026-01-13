package com.k41s.scrollspree.domain.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod {
    @SerialName("Card")
    CARD,

    @SerialName("Paypal")
    PAYPAL,

    @SerialName("Bank_Transfer")
    BANK_TRANSFER,

    @SerialName("Cash")
    CASH
}
