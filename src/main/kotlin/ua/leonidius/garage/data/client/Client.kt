package ua.leonidius.garage.data.client

import javax.persistence.*
import kotlin.properties.Delegates

// data classes aren't supported by spring jpa
@Entity
@Table(name = "clients")
class Client(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val name: String,

    val passportId: String,

    val email: String,

)