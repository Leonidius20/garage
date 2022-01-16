package ua.leonidius.garage.service.car_detail.request_handlers

import ua.leonidius.garage.dto.SearchReturnResult

class MinLengthValidator(private val minLength: Int): BaseHandler() {

    override fun handleSearchQuery(query: String): SearchReturnResult {
        if (query.length < minLength)
            throw IllegalArgumentException("The search query should be at least $minLength symbols long")
        else return getNext()?.handleSearchQuery(query) ?: throw Exception("No handler after validator")
    }

}