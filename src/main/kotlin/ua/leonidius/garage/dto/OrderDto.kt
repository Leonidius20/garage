package ua.leonidius.garage.dto

data class OrderDto(
    val id: Int,
    val user: UserDto,
    val detail: CarDetailDto,
    val quantity: Int,
)