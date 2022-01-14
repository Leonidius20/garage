package ua.leonidius.garage.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.leonidius.garage.business.SearchFacade
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.OrderDto
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.mappers.OrderMapper
import ua.leonidius.garage.model.Order
import ua.leonidius.garage.repository.OrderRepository
import java.util.*

@Service
class OrderService {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderMapper: OrderMapper

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var carDetailService: SearchFacade

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

    fun createOrder(userId: Int, detailId: Int, quantity: Int): OrderDto {
        var order = Order(userId = userId, detailId = detailId, quantity = quantity)
        order = orderRepository.save(order)

        return orderMapper.toDto(order, getUserForOrder(order), getDetailForOrder(order))
    }

    private fun getUserForOrder(order: Order): UserDto {
        return userService.getUserById(order.userId).get() // we trust that the user exists
    }

    private fun getDetailForOrder(order: Order): CarDetailDto {
        return carDetailService.getLocalDetailById(order.detailId)!!
    }

}