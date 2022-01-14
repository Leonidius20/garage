package ua.leonidius.garage.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.leonidius.garage.model.CarDetail

@Repository
interface CarDetailRepository: JpaRepository<CarDetail, Int> {

    fun findByNameContainingIgnoreCase(query: String): List<CarDetail>

}