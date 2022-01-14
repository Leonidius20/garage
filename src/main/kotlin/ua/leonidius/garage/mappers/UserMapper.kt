package ua.leonidius.garage.mappers

import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.model.User

@Component
class UserMapper {

    fun toDto(user: User): UserDto {
        return UserDto(user.id, user.login)
    }

}