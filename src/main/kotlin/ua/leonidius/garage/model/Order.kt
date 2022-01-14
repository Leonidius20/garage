package ua.leonidius.garage.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderId: Int,

    val userId: Int,

    val detailId: Int,

    val quantity: Int,

)