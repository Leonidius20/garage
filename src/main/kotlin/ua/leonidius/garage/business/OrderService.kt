package ua.leonidius.garage.business

import ua.leonidius.garage.data.client.Client

interface OrderService {

    fun createDiagnosticsOrder(isFull: Boolean, client: Client)

    fun createRepairOrder()

    fun getOrder()

}