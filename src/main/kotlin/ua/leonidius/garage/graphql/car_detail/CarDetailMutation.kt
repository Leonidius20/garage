package ua.leonidius.garage.graphql.car_detail

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import kotlinx.coroutines.processNextEventInCurrentThread
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.business.SearchFacade
import ua.leonidius.garage.dto.CarDetailDto
import java.util.*

@Component
class CarDetailMutation: GraphQLMutationResolver {

    @Autowired
    private lateinit var detailService: SearchFacade

    fun createDetail(name: String, manufacturer: String, description: String,
                     type: String, price: Float): CarDetailDto {
        return detailService.addCarDetail(name, manufacturer, description, price, type)
    }

    fun deleteDetail(id: Int): Int {
        detailService.deleteDetail(id)
        return id
    }

    fun updateDetail(id: Int, name: String?, manufacturer: String?, description: String?,
                     type: String?, price: Float?): Optional<CarDetailDto> {
        val detail = detailService.updateLocalDetail(id, name, manufacturer, description, type, price)
        if (detail == null) return Optional.empty()
        else return Optional.of(detail)
    }

}