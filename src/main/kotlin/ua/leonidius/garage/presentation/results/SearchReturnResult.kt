package ua.leonidius.garage.presentation.results

data class SearchReturnResult(
    val results: MutableList<CarDetailReturnResult>,
): ReturnResult

data class CarDetailReturnResult(
    val id: Int? = null,
    val price: Double,
    val name: String,
    val description: String,
    val manufacturer: String,
): ReturnResult