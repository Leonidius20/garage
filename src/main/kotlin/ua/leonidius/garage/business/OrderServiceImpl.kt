package ua.leonidius.garage.business

import org.springframework.stereotype.Service
import ua.leonidius.garage.data.client.Client
import ua.leonidius.garage.data.order.Order

@Service
class OrderServiceImpl: OrderService {



    override fun createDiagnosticsOrder(isFull: Boolean, client: Client) {
        val order = Order.Builder()
                .setDiagnostics(isFull)
                    // todo: car type
                .setClient(client)
                .get()

        // repository save
    }

    override fun createRepairOrder() {

    }

    override fun getOrder() {

    }


}