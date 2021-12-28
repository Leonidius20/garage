package ua.leonidius.garage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult
import java.util.concurrent.ConcurrentHashMap

@EnableAsync
@SpringBootApplication
class GarageApplication {

	companion object {
		val cache = ConcurrentHashMap<String, CarDetailReturnResult>()
		val pageCache = ConcurrentHashMap<Int, Map<String, CarDetailReturnResult>>()
		val searchCache = ConcurrentHashMap<String, Map<String, CarDetailReturnResult>>()
	}

}

fun main(args: Array<String>) {
	runApplication<GarageApplication>(*args)
}
