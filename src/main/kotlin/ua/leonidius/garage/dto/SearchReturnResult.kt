package ua.leonidius.garage.dto

data class SearchReturnResult(
    val results: Collection<CarDetailDto>,
): ReturnResult

