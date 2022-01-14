package ua.leonidius.garage.presentation

import org.springframework.web.bind.annotation.*
import ua.leonidius.garage.dto.ErrorDto
import ua.leonidius.garage.dto.ReturnResult
import ua.leonidius.garage.business.SearchFacade
import ua.leonidius.garage.dto.SearchReturnResult

@RestController
@CrossOrigin(origins = ["*"])
class CarDetailsController(private val searchFacade: SearchFacade) {

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
            return searchFacade.getDetailById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorDto(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("/details")
    fun getAllDetails(@RequestParam page: Int): ReturnResult {
        try {
            return SearchReturnResult(searchFacade.getAllDetails(page))
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorDto(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("/details/search")
    fun searchForDetailsWithFilters(
        @RequestParam query: String,

    ): ReturnResult {
        try {

            return searchFacade.findDetailsByNameWithFilter(query)
        } catch (e: Exception) {
            e.printStackTrace()
            return ErrorDto(e.message!!)
        }
    }

    @CrossOrigin
    @GetMapping("search-cached-details")
    fun searchCachedDetails(@RequestParam query: String): ReturnResult {
        return searchFacade.findDetailsCached(query)
    }

}