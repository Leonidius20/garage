package ua.leonidius.garage.graphql.car_detail

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.business.SearchFacade
import ua.leonidius.garage.dto.CarDetailDto

@Component
class CarDetailMutation: GraphQLMutationResolver {

    @Autowired
    private lateinit var detailService: SearchFacade

    fun createDetail(name: String, manufacturer: String, description: String,
                     type: String, price: Float): CarDetailDto {
        return detailService.addCarDetail(name, manufacturer, description, price, type)
    }

}