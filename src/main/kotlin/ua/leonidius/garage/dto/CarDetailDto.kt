package ua.leonidius.garage.dto

data class CarDetailDto(
    val id: Int? = null,
    var price: Double,
    var name: String,
    var description: String?,
    var manufacturer: String,
    var type: String?,
    var source: String?,
    var fiveThousandPage: Int?
): ReturnResult