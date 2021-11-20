package ua.leonidius.garage.business

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.leonidius.garage.data.client.Client
import ua.leonidius.garage.data.client.ClientRepository

@Service
class ClientServiceImpl: ClientService {

    @Autowired
    private lateinit var repository: ClientRepository

    override fun addClient(name: String, passportId: String, email: String) {
        // TODO: check if such client exists
        repository.save(Client(name = name, passportId = passportId, email = email))
    }

    override fun getClientById(id: Long): Client {
        return repository.findById(id).get()
    }

}