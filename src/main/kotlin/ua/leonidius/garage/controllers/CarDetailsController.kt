package ua.leonidius.garage.controllers

import org.springframework.web.bind.annotation.*
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.ErrorDto
import ua.leonidius.garage.dto.ReturnResult
import ua.leonidius.garage.service.car_detail.CarDetailServiceFacade
import ua.leonidius.garage.dto.SearchReturnResult
import ua.leonidius.garage.service.car_detail.specifications.Specification
import ua.leonidius.garage.service.car_detail.specifications.TrueSpecification
import ua.leonidius.garage.service.car_detail.specifications.сoncrete.ManufacturerSpecification
import ua.leonidius.garage.service.car_detail.specifications.сoncrete.MaxPriceSpecification

@RestController
@CrossOrigin(origins = ["*"])
class CarDetailsController(private val carDetailServiceFacade: CarDetailServiceFacade) {

    /*@CrossOrigin
    @PostMapping("/details")
    fun addCarDetail(
        @RequestParam name: String,
        @RequestParam manufacturer: String,
        @RequestParam description: String,
        @RequestParam price: Double,
        @RequestParam type: String,
    ) {
        searchFacade.addCarDetail(name, manufacturer, description, price, type)
    }*/

    /*@CrossOrigin
    @DeleteMapping("details/{id}")
    fun deleteDetail(@PathVariable id: Int) {
        searchFacade.deleteDetail(id)
    }*/

    @CrossOrigin
    @GetMapping("/detail")
    fun getDetailById(@RequestParam id: String): ReturnResult {
        try {
            val result = carDetailServiceFacade.getDetailById(id)
            if (result == null) return ErrorDto("not found")
            else return result
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorDto(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("/details")
    fun getAllDetails(@RequestParam page: Int): ReturnResult {
        try {
            return SearchReturnResult(carDetailServiceFacade.getAllDetails(page))
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorDto(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("/details/search")
    fun searchForDetailsWithFilters(
        @RequestParam query: String,
        @RequestParam(required = false) maxPrice: Double?,
        @RequestParam(required = false) minPrice: Double?,
        @RequestParam(required = false) manufacturer: String?,
    ): ReturnResult {
        try {
            val filter = createFilter(maxPrice, minPrice, manufacturer)
            return carDetailServiceFacade.findDetailsByNameWithFilter(query, filter)
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorDto(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("search-cached-details")
    fun searchCachedDetails(
        @RequestParam query: String,
        @RequestParam(required = false) maxPrice: Double?,
        @RequestParam(required = false) minPrice: Double?,
        @RequestParam(required = false) manufacturer: String?,
    ): ReturnResult {
        val filter = createFilter(maxPrice, minPrice, manufacturer)
        return carDetailServiceFacade.findDetailsCached(query, filter)
    }

    private fun createFilter(maxPrice: Double?, minPrice: Double?, manufacturer: String?): Specification<CarDetailDto> {
        var filter: Specification<CarDetailDto> = TrueSpecification()

        if (maxPrice != null)
            filter = filter.and(MaxPriceSpecification(maxPrice))

        if (minPrice != null)
            filter = filter.and(MaxPriceSpecification(minPrice).not())

        if (manufacturer != null)
            filter = filter.and(ManufacturerSpecification(manufacturer))

        return filter
    }

}