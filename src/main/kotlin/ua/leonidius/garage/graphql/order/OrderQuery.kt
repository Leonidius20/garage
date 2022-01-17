package ua.leonidius.garage.graphql.order

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.OrderDto
import ua.leonidius.garage.service.order.OrderServiceFacade
import java.util.*

@Component
class OrderQuery: GraphQLQueryResolver {

    @Autowired
    private lateinit var orderService: OrderServiceFacade

    fun getOrderById(id: Int): Optional<OrderDto> {
        if (id < 0) return Optional.empty()
        return orderService.getById(id)
    }

    fun getOrdersByUserId(userId: Int): List<OrderDto> {
        if (userId < 0) return emptyList()
        return orderService.getByUserId(userId)
    }

}