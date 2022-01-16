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

    fun getDetails(page: Int): List<CarDetailDto> {
        return detailService.getAllDetails(page).stream().toList()
    }

    fun getDetailById(id: Int): Optional<CarDetailDto> {
        val value = detailService.getLocalDetailById(id)
        if (value == null) return Optional.empty()
        else return Optional.of(value)
    }

    fun detailsByName(name: String, maxPrice: Float?, minPrice: Float?, manufacturer: String?): List<CarDetailDto> {
        return detailService.findDetailsByNameWithFilter(name, maxPrice, minPrice, manufacturer)
    }

    fun cachedDetailsByName(name: String, maxPrice: Float?, minPrice: Float?, manufacturer: String?): List<CarDetailDto> {
        return detailService.findDetailsCached(name, maxPrice, minPrice, manufacturer)
    }

}