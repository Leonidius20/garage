package ua.leonidius.garage.business.network

import org.springframework.boot.web.client.RestTemplateBuilder
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult

class GetService {

    private val restTemplate = RestTemplateBuilder().build()

    fun get(url: String): SearchReturnResult {
        return restTemplate.getForObject(url, SearchReturnResult::class.java)!!
    }

    fun getOne(url: String): CarDetailReturnResult {
        return restTemplate.getForObject(url, CarDetailReturnResult::class.java)!!
    }

}