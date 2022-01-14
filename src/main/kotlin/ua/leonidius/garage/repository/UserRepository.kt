package ua.leonidius.garage.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.leonidius.garage.model.User
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Int> {

    fun findByLogin(login: String): Optional<User>

}