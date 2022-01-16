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
        @RequestParam(required = false) maxPrice: Float?,
        @RequestParam(required = false) minPrice: Float?,
        @RequestParam(required = false) manufacturer: String?,
    ): ReturnResult {
        try {
            return SearchReturnResult(
                carDetailServiceFacade.findDetailsByNameWithFilter(query, maxPrice, minPrice, manufacturer)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorDto(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("search-cached-details")
    fun searchCachedDetails(
        @RequestParam query: String,
        @RequestParam(required = false) maxPrice: Float?,
        @RequestParam(required = false) minPrice: Float?,
        @RequestParam(required = false) manufacturer: String?,
    ): ReturnResult {
        return SearchReturnResult(
            carDetailServiceFacade.findDetailsCached(query, maxPrice, minPrice, manufacturer)
        )
    }



}