package com.k41s.scrollspree.data.mapper

import com.k41s.scrollspree.data.remote.dto.CartDTO
import com.k41s.scrollspree.domain.model.Product

fun mapDtoToState(cartDto: CartDTO): Map<Product, Int> {
    return cartDto.items.associate { itemDto ->
        // Reuses your existing toDomain() mapper for the ProductDTO
        itemDto.product.toDomain() to itemDto.quantity
    }
}