package com.k41s.scrollspree.data.remote.dto

import com.k41s.scrollspree.domain.model.enums.PaymentMethod
import kotlinx.datetime.LocalDateTime

data class OrderDTO(
    val id: Int? = null,
    val productId: Int? = null,
    val productName: String,
    val isProductDeleted: Boolean,
    val userId: Int? = null,
    val userName: String,
    val orderedAt: LocalDateTime? = null,
    val paymentMethod: PaymentMethod,
    val notes: String,
    val mainImageId: Int? = null
)
