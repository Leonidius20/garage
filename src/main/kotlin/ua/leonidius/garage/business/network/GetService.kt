package ua.leonidius.garage.business.network

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult
import java.util.concurrent.CompletableFuture

@Service
open class GetService {

    private val restTemplate = RestTemplateBuilder().build()

    @Async
    open fun get(url: String): CompletableFuture<SearchReturnResult> {
        return CompletableFuture.completedFuture(
            restTemplate.getForObject(url, SearchReturnResult::class.java)!!)
    }

    fun getOne(url: String): CarDetailReturnResult {
        return restTemplate.getForObject(url, CarDetailReturnResult::class.java)!!
    }

}