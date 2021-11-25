package ua.leonidius.garage.business

import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult
import ua.leonidius.garage.business.specifications.Specification

interface SearchFacade {

    fun getAllDetails(): SearchReturnResult

    fun findDetailsByNameWithFilter(
        name: String, filter: Specification<CarDetailReturnResult>
    ): SearchReturnResult

    fun addCarDetail(name: String, manufacturer: String,
                     description: String, price: Double, type: String)

    fun getDetailById(id: Int): CarDetailReturnResult

    fun deleteDetail(id: Int)

}