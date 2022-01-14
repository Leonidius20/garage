package ua.leonidius.garage.business.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult
import java.util.concurrent.CompletableFuture

@Service
open class GetService {

    private val restTemplate = RestTemplateBuilder().build()

    suspend fun get(url: String): SearchReturnResult {
        return withContext(Dispatchers.IO) {
            return@withContext restTemplate.getForObject(url, SearchReturnResult::class.java)!!
        }
    }

    suspend fun getOne(url: String): CarDetailReturnResult {
        return withContext(Dispatchers.IO) {
            return@withContext restTemplate.getForObject(url, CarDetailReturnResult::class.java)!!
        }
    }

}