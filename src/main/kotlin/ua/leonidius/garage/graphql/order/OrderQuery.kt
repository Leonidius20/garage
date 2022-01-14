package ua.leonidius.garage.graphql.order

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.OrderDto
import ua.leonidius.garage.service.OrderService
import java.util.*

@Component
class OrderQuery: GraphQLQueryResolver {

    @Autowired
    private lateinit var orderService: OrderService

    fun getOrderById(id: Int): Optional<OrderDto> {
        return orderService.getById(id)
    }

    fun getOrdersByUserId(userId: Int): List<OrderDto> {
        return orderService.getByUserId(userId)
    }

}