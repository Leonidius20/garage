package ua.leonidius.garage.graphql.car_detail

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.business.SearchFacade
import ua.leonidius.garage.dto.CarDetailDto
import java.util.*

@Component
class CarDetailQuery : GraphQLQueryResolver {

    @Autowired
    private lateinit var detailService: SearchFacade

    fun getDetails(count: Int): List<CarDetailDto> {
        return detailService.getAllDetails(count).stream().toList()
    }

    fun getDetailById(id: Int): Optional<CarDetailDto> {
        return Optional.of(detailService.getLocalDetailById(id))
    }

}