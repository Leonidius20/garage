package ua.leonidius.garage.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/order")
class OrdersController {

    @GetMapping
    fun getOrder() {
        // TODO; remove this, insted work with graphQL
    }

}