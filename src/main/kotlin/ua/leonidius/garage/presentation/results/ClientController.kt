package ua.leonidius.garage.presentation.results

import org.springframework.web.bind.annotation.*
import ua.leonidius.garage.data.client.Client
import ua.leonidius.garage.data.client.ClientRepository

data class ClientReturn(val id: String, val name: String,
                        val passportId: String, val email: String)

@RestController
class ClientController(private val repository: ClientRepository) {

    @PutMapping("/client")
    fun addClient(
        @RequestParam(value = "name") name: String,
        @RequestParam(value = "passportId") passportId: String,
        @RequestParam(value = "email") email: String,
    ) {
        repository.save(Client(name = name, passportId = passportId, email = email))
    }

    @GetMapping("/client/{id}")
    fun getClient(@PathVariable(value="id") id: Long): Client {
        // TODO: business layer
        return  repository.findById(id).get()
    }

}