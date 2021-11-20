package ua.leonidius.garage.presentation.results

import org.springframework.web.bind.annotation.*
import ua.leonidius.garage.business.ClientService
import ua.leonidius.garage.data.client.Client
import ua.leonidius.garage.data.client.ClientRepository

data class ClientReturn(val id: String, val name: String,
                        val passportId: String, val email: String)

@RestController
class ClientController(private val service: ClientService) {

    @PutMapping("/client")
    fun addClient(
        @RequestParam(value = "name") name: String,
        @RequestParam(value = "passportId") passportId: String,
        @RequestParam(value = "email") email: String,
    ) {
        // TODO: validate input
        service.addClient(name, passportId, email)
    }

    @GetMapping("/client/{id}")
    fun getClient(@PathVariable(value="id") id: Long): Client {
        // TODO: validate input
        return service.getClientById(id)
    }

}