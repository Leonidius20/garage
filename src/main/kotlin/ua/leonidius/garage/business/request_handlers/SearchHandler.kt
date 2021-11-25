package ua.leonidius.garage.business.request_handlers

import ua.leonidius.garage.data.car_details.CarDetailRepository
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult

class SearchHandler(private val repository: CarDetailRepository): BaseHandler() {

    override fun handleSearchQuery(query: String): SearchReturnResult {
        // TODO: search in the other 2
        val resultSet = repository.findByNameContainingIgnoreCase(query).map {
            CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer)
        }.toMutableList()
        return getNext()?.handleSearchQuery(query) ?: SearchReturnResult(resultSet)
    }

}