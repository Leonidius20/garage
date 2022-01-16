package ua.leonidius.garage.service.car_detail

import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.SearchReturnResult
import ua.leonidius.garage.service.car_detail.specifications.Specification

interface CarDetailServiceFacade {

    fun getAllDetails(page: Int = 0): Collection<CarDetailDto>

    fun findDetailsByNameWithFilter(
        name: String,
        filter: Specification<CarDetailDto>
    ): SearchReturnResult

    fun addCarDetail(name: String, manufacturer: String,
                     description: String, price: Float, type: String): CarDetailDto

    fun getDetailById(id: String): CarDetailDto?

    //fun deleteDetail(id: Int)

    fun findDetailsCached(name: String, filter: Specification<CarDetailDto>): SearchReturnResult

    fun getLocalDetailById(id: Int): CarDetailDto?
    fun deleteDetail(id: Int)

    fun updateLocalDetail(id: Int, name: String?, manufacturer: String?, description: String?,
                          type: String?, price: Float?): CarDetailDto?

}