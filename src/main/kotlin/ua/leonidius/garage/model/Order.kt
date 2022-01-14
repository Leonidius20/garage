package ua.leonidius.garage.model

import javax.persistence.*

@Entity(name = "orders")
@Table(name = "orders")
class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderId: Int? = null,

    val userId: Int,

    val detailId: Int,

    val quantity: Int,

)