package ua.leonidius.garage.model

import javax.persistence.*

@Entity(name = "users")
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val login: String,

    val password: String,



)