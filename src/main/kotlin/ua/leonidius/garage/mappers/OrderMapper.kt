package ua.leonidius.garage.mappers

import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.OrderDto
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.model.Order

@Component
class OrderMapper {

    fun toDto(order: Order, userDto: UserDto, carDetailDto: CarDetailDto): OrderDto {
        return OrderDto(order.orderId!!, userDto, carDetailDto, order.quantity)
    }

}