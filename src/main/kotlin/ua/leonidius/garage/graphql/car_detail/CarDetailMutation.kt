package ua.leonidius.garage.graphql.car_detail

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.service.car_detail.CarDetailServiceFacade
import ua.leonidius.garage.dto.CarDetailDto
import java.util.*

@Component
class CarDetailMutation: GraphQLMutationResolver {

    @Autowired
    private lateinit var detailService: CarDetailServiceFacade

    fun createDetail(name: String, manufacturer: String, description: String,
                     type: String, price: Float): CarDetailDto? {
        if (price < 0) return null
        return detailService.addCarDetail(name, manufacturer, description, price, type)
    }

    fun deleteDetail(id: Int, source: String): Int? {
        if (id < 0) return null
        if (!detailService.deleteDetail(id, source)) return null
        return id
    }

    fun updateDetail(id: Int, source: String, name: String?, manufacturer: String?, description: String?,
                     type: String?, price: Float?): Optional<CarDetailDto> {
        if (id < 0 || (price != null && price < 0)) return Optional.empty()

        val detail = detailService.updateDetail(id, source, name, manufacturer, description, type, price)
        if (detail == null) return Optional.empty()
        else return Optional.of(detail)
    }

}