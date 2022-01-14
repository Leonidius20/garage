package ua.leonidius.garage.dto

data class CarDetailDto(
    val id: Int? = null,
    val price: Double,
    val name: String,
    val description: String?,
    val manufacturer: String,
    val type: String?,
    var source: String?,
): ReturnResult