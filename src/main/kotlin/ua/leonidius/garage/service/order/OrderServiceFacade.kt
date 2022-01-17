package ua.leonidius.garage.service.order

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.OrderDto
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.exception.InvalidIdException
import ua.leonidius.garage.mappers.OrderMapper
import ua.leonidius.garage.model.Order
import ua.leonidius.garage.repository.OrderRepository
import ua.leonidius.garage.service.user.UserServiceFacade
import ua.leonidius.garage.service.car_detail.CarDetailServiceFacade
import java.util.*

@Service
class OrderServiceFacade {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderMapper: OrderMapper

    @Autowired
    private lateinit var userService: UserServiceFacade

    @Autowired
    private lateinit var carDetailService: CarDetailServiceFacade

    fun getById(id: Int): Optional<OrderDto> {
        val orderOpt = orderRepository.findById(id)
        if (orderOpt.isEmpty) return Optional.empty()

        val order = orderOpt.get()

        return Optional.of(orderMapper.toDto(order,
            getUserForOrder(order), getDetailForOrder(order)))
    }

    fun getByUserId(userId: Int): List<OrderDto> {
        return orderRepository.findByUserId(userId).map {
            orderMapper.toDto(it, getUserForOrder(it), getDetailForOrder(it))
        }
    }

    fun createOrder(userId: Int, detailId: Int, quantity: Int, source: String): OrderDto {
        if (!userService.userExists(userId)) {
            throw InvalidIdException("User with id $userId doesn't exist")
        }

        if (source != "local" && source != "8088" && source != "8082") {
            throw InvalidIdException("Invalid source")
        }

        if (!carDetailService.carDetailExists(detailId, source)) {
            throw InvalidIdException("No detail with ID $detailId in source $source")
        }

        var order = Order(userId = userId, detailId = detailId, quantity = quantity, source = source)
        order = orderRepository.save(order)

        return orderMapper.toDto(order, getUserForOrder(order), getDetailForOrder(order))
    }

    private fun getUserForOrder(order: Order): UserDto {
        return userService.getUserById(order.userId).get() // we trust that the user exists
    }

    private fun getDetailForOrder(order: Order): CarDetailDto {
        return carDetailService.getDetailById("${order.detailId}-${order.source}")!!
    }

}