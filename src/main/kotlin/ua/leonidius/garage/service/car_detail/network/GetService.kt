package ua.leonidius.garage.service.car_detail.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.SearchReturnResult

@Service
open class GetService {

    private val restTemplate = RestTemplateBuilder().build()

    suspend fun get(url: String): SearchReturnResult {
        return withContext(Dispatchers.IO) {
            return@withContext restTemplate.getForObject(url, SearchReturnResult::class.java)!!
        }
    }

    suspend fun getOne(url: String): CarDetailDto {
        return withContext(Dispatchers.IO) {
            return@withContext restTemplate.getForObject(url, CarDetailDto::class.java)!!
        }
    }

    suspend fun getOneWithParams(url: String, params: Map<String, *>): CarDetailDto? {
        return withContext(Dispatchers.IO) {
            return@withContext restTemplate.getForObject(url, CarDetailDto::class.java, params)!!
        }
    }

    suspend fun getInteger(url: String): Int {
        return withContext(Dispatchers.IO) {
            return@withContext restTemplate.getForObject(url, Int::class.java)!!
        }
    }

    fun delete(url: String): Boolean {
        return restTemplate.getForObject(url, Boolean::class.java)!!
    }

}