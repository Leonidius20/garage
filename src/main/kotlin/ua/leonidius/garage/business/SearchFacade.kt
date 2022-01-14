package ua.leonidius.garage.business

import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.SearchReturnResult

interface SearchFacade {

    fun getAllDetails(page: Int = 0): Collection<CarDetailDto>

    fun findDetailsByNameWithFilter(
        name: String
    ): SearchReturnResult

    fun addCarDetail(name: String, manufacturer: String,
                     description: String, price: Float, type: String): CarDetailDto

    fun getDetailById(id: String): CarDetailDto?

    //fun deleteDetail(id: Int)

    fun findDetailsCached(name: String): SearchReturnResult

    fun getLocalDetailById(id: Int): CarDetailDto?
    fun deleteDetail(id: Int)

    fun updateLocalDetail(id: Int, name: String?, manufacturer: String?, description: String?,
                          type: String?, price: Float?): CarDetailDto?

}