package ua.leonidius.garage.data.car_details

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "car_details")
class CarDetail(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    var carTypeId: Int,

    var detailTypeId: Int? = null,

    var detailTypeCustomName: String? = null,

    var manufacturer: String,

    var description: String?,

    var price: Double,

    var name: String,

) {

    class Builder {

        private val detail = CarDetail(carTypeId = -1, manufacturer = "", description = "", price = -1.0, name = "")

        fun setName(name: String): Builder {
            detail.name = name
            return this
        }

        fun setDescription(description: String): Builder {
            detail.description = description
            return this
        }

        fun setManufacturer(manufacturer: String): Builder {
            detail.manufacturer = manufacturer
            return this
        }

        fun setDetailTypeId(id: Int): Builder {
            detail.detailTypeCustomName = null
            detail.detailTypeId = id
            return this
        }

        fun setDetailCustomType(name: String): Builder {
            detail.detailTypeId = null
            detail.detailTypeCustomName = name
            return this
        }

        fun setCarTypeId(id: Int): Builder {
            detail.carTypeId = id
            return this
        }

        fun setPrice(price: Double): Builder {
            detail.price = price
            return this
        }

        fun get() = detail

    }

}