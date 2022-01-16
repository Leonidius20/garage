package ua.leonidius.garage.service.car_detail.request_handlers

import ua.leonidius.garage.dto.SearchReturnResult

interface RequestHandler {

    fun handleSearchQuery(query: String): SearchReturnResult

    fun getNext(): RequestHandler?

    fun setNext(handler: RequestHandler)

}