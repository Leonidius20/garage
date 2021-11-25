package ua.leonidius.garage.business.request_handlers

import ua.leonidius.garage.presentation.results.SearchReturnResult

class IllegalCharsValidator: BaseHandler() {

    override fun handleSearchQuery(query: String): SearchReturnResult {
        val illegalChars = "~#@*+%{}<>[]|“”\\^"
        if (illegalChars.any { query.contains(it) })
            throw IllegalArgumentException("The query contains illegal characters")
        else return getNext()?.handleSearchQuery(query) ?: throw Exception("No handler after validator")
    }

}