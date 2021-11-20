package ua.leonidius.garage.data.client

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface ClientRepository: JpaRepository<Client, Long> {



}