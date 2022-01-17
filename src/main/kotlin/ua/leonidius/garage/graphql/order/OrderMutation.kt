package ua.leonidius.garage.graphql.order

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.OrderDto
import ua.leonidius.garage.service.order.OrderServiceFacade

@Component
class OrderMutation: GraphQLMutationResolver {

    @Autowired
    private lateinit var orderService: OrderServiceFacade

    fun createOrder(userId: Int, detailId: Int, quantity: Int, source: String): OrderDto? {
        if (userId < 0 || detailId < 0 || quantity < 0) return null
        return orderService.createOrder(userId, detailId, quantity, source)
    }

}