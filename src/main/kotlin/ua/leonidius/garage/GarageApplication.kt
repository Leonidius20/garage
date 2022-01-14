package ua.leonidius.garage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import ua.leonidius.garage.dto.CarDetailDto
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@EnableAsync
@EnableScheduling
@SpringBootApplication
class GarageApplication {

	companion object {
		val cache = ConcurrentHashMap<String, Pair<LocalDate, CarDetailDto>>()
		val pageCache = ConcurrentHashMap<Int, Pair<LocalDate, Collection<CarDetailDto>>>()
		// val searchCache = ConcurrentHashMap<String, Pair<LocalDate, Collection<CarDetailReturnResult>>>()
	}

	@Scheduled(fixedDelay = 86400000, initialDelay = 0) // fixedDelay = 24h
	fun clearOldCache() {
		cache.clear()
		pageCache.clear()
		// searchCache.clear()
	}

	@Scheduled(fixedDelay = 3600000, initialDelay = 3600000) // every hour
	fun clearLargeCache() {
		if (cache.size > 10000) cache.clear()
		if (pageCache.size > 10000 / 15) pageCache.clear() // 15 items per page
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
