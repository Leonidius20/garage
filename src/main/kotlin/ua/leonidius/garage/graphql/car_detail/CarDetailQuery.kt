package ua.leonidius.garage.graphql.car_detail

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.service.car_detail.CarDetailServiceFacade
import ua.leonidius.garage.dto.CarDetailDto
import java.util.*

@Component
class CarDetailQuery : GraphQLQueryResolver {

    @Autowired
    private lateinit var detailService: CarDetailServiceFacade

    fun getDetails(page: Int): List<CarDetailDto>? {
        if (page < 1) return null
        return detailService.getAllDetails(page).stream().toList()
    }

    fun getDetailById(id: Int, source: String): Optional<CarDetailDto> {
        try {
            val value = detailService.getDetailById("$id-$source")
            if (value == null) return Optional.empty()
            else return Optional.of(value)
        } catch (e: Exception) {
            return Optional.empty()
        }
    }

    fun detailsByName(name: String, maxPrice: Float?, minPrice: Float?, manufacturer: String?): List<CarDetailDto> {
        if (name.isEmpty()) return emptyList()
        return detailService.findDetailsByNameWithFilter(name, maxPrice, minPrice, manufacturer)
    }

    /*fun cachedDetailsByName(name: String, maxPrice: Float?, minPrice: Float?, manufacturer: String?): List<CarDetailDto> {
        return detailService.findDetailsCached(name, maxPrice, minPrice, manufacturer)
    }*/

}