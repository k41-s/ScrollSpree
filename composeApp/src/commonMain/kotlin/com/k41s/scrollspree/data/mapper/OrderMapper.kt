package com.k41s.scrollspree.data.mapper

import com.k41s.scrollspree.data.remote.dto.OrderDTO
import com.k41s.scrollspree.data.remote.dto.OrderItemDTO
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.domain.model.OrderItem

fun OrderItemDTO.toDomain(): OrderItem =
    OrderItem(
        productId = this.productId,
        productName = this.productName,
        price = this.price,
        quantity = this.quantity,
        mainImageId = this.mainImgId,
        isDeleted = this.isProductDeleted
    )

fun OrderDTO.toDomain(): Order =
    Order(
        id = this.id ?: -1,
        items = this.items.map { it.toDomain() },
        userId = this.userId ?: -1,
        userName = this.userName,
        orderedAt = this.orderedAt,
        paymentMethod = this.paymentMethod,
        notes = this.notes
    )