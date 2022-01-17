package ua.leonidius.garage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*


@EnableAsync
@EnableScheduling
@SpringBootApplication
class GarageApplication {

	@Bean
	fun corsFilter(): CorsFilter {
		val source = UrlBasedCorsConfigurationSource()
		val config = CorsConfiguration()
		config.allowCredentials = true
		config.allowedOrigins = Collections.singletonList("http://localhost:8080/")
		config.allowedHeaders = Arrays.asList("Origin", "Content-Type", "Accept")
		config.allowedMethods = Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
		source.registerCorsConfiguration("/**", config)
		return CorsFilter(source)
	}

}

fun main(args: Array<String>) {
	runApplication<GarageApplication>(*args)
}
