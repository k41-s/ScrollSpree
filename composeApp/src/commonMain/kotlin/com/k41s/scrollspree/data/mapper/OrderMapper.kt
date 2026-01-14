package com.k41s.scrollspree.data.mapper

import com.k41s.scrollspree.data.remote.dto.OrderDTO
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.domain.model.OrderProduct

fun OrderDTO.toDomain(): Order =
    Order(
        id = this.id ?: -1,
        product = OrderProduct(
            id = this.productId ?: -1,
            name = this.productName,
            isDeleted = this.isProductDeleted,
            mainImageId = this.mainImageId
        ),
        userId = this.userId ?: -1,
        userName = this.userName,
        orderedAt = this.orderedAt,
        paymentMethod = this.paymentMethod,
        notes = this.notes
    )