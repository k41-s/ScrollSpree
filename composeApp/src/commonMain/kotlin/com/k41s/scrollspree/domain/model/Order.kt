package com.k41s.scrollspree.domain.model

import com.k41s.scrollspree.domain.model.enums.PaymentMethod
import kotlinx.datetime.LocalDateTime

data class Order(
    val id: Int,
    val items: List<OrderItem>,
    val userId: Int,
    val userName: String,
    val orderedAt: LocalDateTime?,
    val paymentMethod: PaymentMethod,
    val notes: String
)
