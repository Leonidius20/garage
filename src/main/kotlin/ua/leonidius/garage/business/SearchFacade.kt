package ua.leonidius.garage.business

import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.SearchReturnResult

interface SearchFacade {

    fun getAllDetails(page: Int = 0): Collection<CarDetailDto>

    fun findDetailsByNameWithFilter(
        name: String
    ): SearchReturnResult

    //fun addCarDetail(name: String, manufacturer: String,
    //                 description: String, price: Double, type: String)

    fun getDetailById(id: String): CarDetailDto

    //fun deleteDetail(id: Int)

    fun findDetailsCached(name: String): SearchReturnResult

}