package ua.leonidius.garage.business

import ua.leonidius.garage.data.client.Client

interface ClientService {

    fun addClient(name: String, passportId: String, email: String)

    fun getClientById(id: Long): Client

}