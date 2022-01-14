package ua.leonidius.garage.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.mappers.UserMapper
import ua.leonidius.garage.repository.UserRepository

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userMapper: UserMapper

    fun getAllUsers(page: Int): List<UserDto> {
        return userRepository.findAll(PageRequest.of(page, 5)).map {
            userMapper.toDto(it)
        }.toList()
    }

}