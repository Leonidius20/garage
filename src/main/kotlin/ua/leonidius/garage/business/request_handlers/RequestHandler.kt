package ua.leonidius.garage.business.request_handlers

import ua.leonidius.garage.dto.SearchReturnResult

interface RequestHandler {

    fun handleSearchQuery(query: String): SearchReturnResult

    fun getNext(): RequestHandler?

    fun setNext(handler: RequestHandler)

}