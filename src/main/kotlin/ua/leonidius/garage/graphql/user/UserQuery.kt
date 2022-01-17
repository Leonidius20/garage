package ua.leonidius.garage.graphql.user

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.service.user.UserServiceFacade
import java.util.*

@Component
class UserQuery: GraphQLQueryResolver {

    @Autowired
    private lateinit var userService: UserServiceFacade

    fun getUsers(page: Int): List<UserDto>? {
        if (page < 0) return null
        return userService.getAllUsers(page)
    }

    fun getUserById(id: Int): Optional<UserDto> {
        if (id < 0) return Optional.empty()
        return userService.getUserById(id)
    }

    fun getUserByLogin(login: String): Optional<UserDto> {
        if (login.isEmpty()) return Optional.empty()
        return userService.getUserByLogin(login)
    }

    /*fun getDetailById(id: Int): Optional<CarDetailDto> {
        val value = detailService.getLocalDetailById(id)
        if (value == null) return Optional.empty()
        else return Optional.of(value)
    }*/

}