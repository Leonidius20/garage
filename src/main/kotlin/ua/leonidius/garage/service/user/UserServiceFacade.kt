package ua.leonidius.garage.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ua.leonidius.garage.dto.UserDto
import ua.leonidius.garage.mappers.UserMapper
import ua.leonidius.garage.repository.UserRepository
import java.util.*

@Service
class UserServiceFacade {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userMapper: UserMapper

    fun getAllUsers(page: Int): List<UserDto> {
        return userRepository.findAll(PageRequest.of(page, 5)).map {
            userMapper.toDto(it)
        }.toList()
    }

    fun getUserById(id: Int): Optional<UserDto> {
        val user = userRepository.findById(id)
        if (user.isEmpty) return Optional.empty()
        else return Optional.of(userMapper.toDto(user.get()))
    }

    fun getUserByLogin(login: String): Optional<UserDto> {
        val user = userRepository.findByLogin(login)
        if (user.isEmpty) return Optional.empty()
        else return Optional.of(userMapper.toDto(user.get()))
    }

    fun userExists(id: Int) = userRepository.existsById(id)

}