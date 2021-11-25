package ua.leonidius.garage.presentation

import org.springframework.web.bind.annotation.*
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.ErrorReturnResult
import ua.leonidius.garage.presentation.results.ReturnResult
import ua.leonidius.garage.business.SearchFacade
import ua.leonidius.garage.business.specifications.Specification
import ua.leonidius.garage.business.specifications.TrueSpecification
import ua.leonidius.garage.business.specifications.сoncrete.ManufacturerSpecification
import ua.leonidius.garage.business.specifications.сoncrete.MaxPriceSpecification

@RestController
class CarDetailsController(private val searchFacade: SearchFacade) {

    @CrossOrigin
    @PostMapping("/details")
    fun addCarDetail(
        @RequestParam name: String,
        @RequestParam manufacturer: String,
        @RequestParam description: String,
        @RequestParam price: Double,
        @RequestParam type: String,
    ) {
        searchFacade.addCarDetail(name, manufacturer, description, price, type)
    }

    @CrossOrigin
    @GetMapping("/details/{id}")
    fun getDetailById(@PathVariable id: Int): ReturnResult {
        try {
            return searchFacade.getDetailById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorReturnResult(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("/details")
    fun getAllDetails(): ReturnResult {
        try {
            return searchFacade.getAllDetails()
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorReturnResult(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("/details/search")
    fun searchForDetailsWithFilters(
        @RequestParam query: String,
        @RequestParam(required = false) maxPrice: Double?,
        @RequestParam(required = false) minPrice: Double?,
        @RequestParam(required = false) manufacturer: String?
    ): ReturnResult {
        try {
            var filter: Specification<CarDetailReturnResult> = TrueSpecification()

            if (maxPrice != null)
                filter = filter.and(MaxPriceSpecification(maxPrice))

            if (minPrice != null)
                filter = filter.and(MaxPriceSpecification(minPrice).not())

            if (manufacturer != null)
                filter = filter.and(ManufacturerSpecification(manufacturer))

            return searchFacade.findDetailsByNameWithFilter(query, filter)
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorReturnResult(e.message!!)
        }
    }

}