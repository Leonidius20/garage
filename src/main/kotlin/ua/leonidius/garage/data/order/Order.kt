package ua.leonidius.garage.data.order

import ua.leonidius.garage.data.client.Client
import javax.persistence.*
import kotlin.properties.Delegates

@Entity
class Order (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var clientId: Long,

    var carTypeId: Long,

    var isDiagnostics: Boolean, // if false -it's a repair

    var estimatedCost: Float? = null, // for repairs

    var estimatedDuration: Float? = null, // hours, for repairs

    var isDiagnosticsFull: Boolean? = null, // for diagnostics

    var report: String? = null, // for diag

) {

    class Builder {

        private val order = Order(null, -1, -1, false)

        fun setDiagnostics(full: Boolean): Builder {
            order.isDiagnostics = true
            order.isDiagnosticsFull = full
            return this
        }

        fun setRepair(): Builder {
            order.isDiagnostics = false
            return this
        }

        fun setClient(client: Client): Builder {
            order.clientId = client.id!!
            return this
        }

        fun setCarType(carTypeId: Long): Builder {
            order.carTypeId = carTypeId // TODO: replace with car type object
            return this
        }

        fun setEstimatedCost(cost: Float): Builder {
            order.estimatedCost = cost
            return this
        }

        fun setEstimatedDuration(duration: Float): Builder {
            order.estimatedDuration = duration
            return this
        }

        fun get(): Order = order

    }

}