package ua.leonidius.garage.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.leonidius.garage.model.Order

@Repository
interface OrderRepository: JpaRepository<Order, Int> {



}