package ua.leonidius.garage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@EnableAsync
@SpringBootApplication
class GarageApplication {

	companion object {
		// add date
		val cache = ConcurrentHashMap<String, Pair<LocalDate, CarDetailReturnResult>>()
		val pageCache = ConcurrentHashMap<Int, Pair<LocalDate, Collection<CarDetailReturnResult>>>()
		val searchCache = ConcurrentHashMap<String, Pair<LocalDate, Collection<CarDetailReturnResult>>>()
	}

	@Bean
	fun corsFilter(): CorsFilter {
		val source = UrlBasedCorsConfigurationSource()
		val config = CorsConfiguration()
		config.allowCredentials = true
		// Don't do this in production, use a proper list  of allowed origins
		config.allowedOrigins = Collections.singletonList("http://localhost:8080/")
		config.allowedHeaders = Arrays.asList("Origin", "Content-Type", "Accept")
		config.allowedMethods = Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
		source.registerCorsConfiguration("/**", config)
		return CorsFilter(source)
	}



}

fun main(args: Array<String>) {
	runApplication<GarageApplication>(*args)
	val x = GraphQLProvider()
}
