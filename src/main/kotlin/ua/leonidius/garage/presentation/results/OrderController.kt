package ua.leonidius.garage.presentation.results

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class Order(val id: Long, val text: String)

@RestController
class OrderController {

    @GetMapping("/order")
    fun getOrder(@RequestParam(value = "id", defaultValue = "0") id: String): Order {
        return Order(0, "eh")
    }

}