package ua.leonidius.garage.business

import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult
import ua.leonidius.garage.business.specifications.Specification

interface SearchFacade {

    fun getAllDetails(page: Int = 0): Collection<CarDetailReturnResult>

    fun findDetailsByNameWithFilter(
        name: String
    ): SearchReturnResult

    //fun addCarDetail(name: String, manufacturer: String,
    //                 description: String, price: Double, type: String)

    fun getDetailById(id: String): CarDetailReturnResult

    //fun deleteDetail(id: Int)

    fun findDetailsCached(name: String): SearchReturnResult

}