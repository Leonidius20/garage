package ua.leonidius.garage.graphql.order

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.OrderDto
import ua.leonidius.garage.service.OrderService

@Component
class OrderMutation: GraphQLMutationResolver {

    @Autowired
    private lateinit var orderService: OrderService

    fun createOrder(userId: Int, detailId: Int, quantity: Int): OrderDto {
        return orderService.createOrder(userId, detailId, quantity)
    }

}