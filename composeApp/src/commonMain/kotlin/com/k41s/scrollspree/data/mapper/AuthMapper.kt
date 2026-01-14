package com.k41s.scrollspree.data.mapper

import com.k41s.scrollspree.data.remote.dto.AuthenticatedUserDTO
import com.k41s.scrollspree.data.remote.dto.UserDTO
import com.k41s.scrollspree.data.remote.dto.UserWithOrdersDTO
import com.k41s.scrollspree.domain.model.User

fun AuthenticatedUserDTO.toDomain() = User(
    username = username,
    email = email,
    firstName = name,
    lastName = surname,
    phone = phone,
    role = role,
    token = token
)

fun UserDTO.toDomain() = User(
    id = id,
    username = username,
    email = email,
    firstName = name,
    lastName = surname,
    phone = phone,
    role = role
)

fun UserWithOrdersDTO.toDomain() = User(
    username = username,
    firstName = name,
    lastName = surname,
    role = role,
    email = "Not Provided",
    phone = "Not Provided",
    orders = orders.map { it.toDomain() }
)