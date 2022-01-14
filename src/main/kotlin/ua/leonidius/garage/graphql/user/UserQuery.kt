package ua.leonidius.garage.graphql.user

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.service.UserService
import java.util.*

@Component
class UserQuery: GraphQLQueryResolver {

    @Autowired
    private lateinit var userService: UserService

    fun getUsers(page: Int): List<UserDto> {
        return userService.getAllUsers(page)
    }

    /*fun getDetailById(id: Int): Optional<CarDetailDto> {
        val value = detailService.getLocalDetailById(id)
        if (value == null) return Optional.empty()
        else return Optional.of(value)
    }*/

}