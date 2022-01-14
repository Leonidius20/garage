package ua.leonidius.garage.business

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ua.leonidius.garage.GarageApplication
import ua.leonidius.garage.business.network.GetService
import ua.leonidius.garage.data.car_details.CarDetailRepository
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

@Service
class SearchFacadeImpl : SearchFacade {

    @Autowired
    private lateinit var repository: CarDetailRepository

    private val getService = GetService()

    private val SLOW_SERVICE_URL = "http://localhost:8088"

    override fun getAllDetails(page: Int): Collection<CarDetailReturnResult>  {

        if (GarageApplication.pageCache.containsKey(page)) {
            val entry = GarageApplication.pageCache[page]!!
            if (ChronoUnit.DAYS.between(LocalDate.now(), entry.first) <= 1) {
                return entry.second
            }
        }

        val results = java.util.Collections.synchronizedList(mutableListOf<CarDetailReturnResult>())

        runBlocking {
            val tasks = listOf(
                launch(Dispatchers.IO) {
                    // results from slow service
                    results.addAll(getService
                        .get("${SLOW_SERVICE_URL}/price-list?page=$page")
                        .results.map { it.apply { source = "8088" } })
                },
                launch(Dispatchers.IO) {
                    // local results
                    results.addAll(repository.findAll(PageRequest.of(page, 5)).map {
                        CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer, "local")
                    })
                },
                launch(Dispatchers.IO) {
                    // results from 5000 service
                    results.addAll(getService.get(
                        "http://localhost:8082/details?page=${page % 500}"
                    ).results.map { it.apply { source = "8082" } })
                },
            )

            tasks.joinAll()
        }

        GarageApplication.cache.putAll(
            results.map { Pair(LocalDate.now(), it) }
                .associateBy { "${it.second.id}-${it.second.source}" })


        GarageApplication.pageCache.put(page, Pair(LocalDate.now(), results))

        return  results
    }

    @Cacheable("results")
    override fun findDetailsByNameWithFilter(
        name: String
    ): SearchReturnResult {

        if (GarageApplication.searchCache.containsKey(name)) {
            val entry = GarageApplication.searchCache[name]!!
            if (ChronoUnit.DAYS.between(LocalDate.now(), entry.first) <= 1) {
                return SearchReturnResult(entry.second)
            } // else refetch
        }

        val results = mutableListOf<CarDetailReturnResult>()

        /*val slowResults = getService.get( // slow service
            "http://localhost:8088/search?query=$name"
        ).get().results.map { it.apply { source = "8088" } }
        results.addAll(slowResults)
        GarageApplication.cache.putAll(slowResults.map { Pair(LocalDate.now(), it) }.associateBy { "${it.second.id}-8088" })

        // local DB
        val localResults = repository.findByNameContainingIgnoreCase(name).map {
            CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer, "local")
        }
        results.addAll(localResults)
        GarageApplication.cache.putAll(localResults.map { Pair(LocalDate.now(), it) }.associateBy { "${it.second.id}-local" })

        GarageApplication.searchCache[name] = Pair(LocalDate.now(), results)*/

        return SearchReturnResult(results)
    }

    override fun getDetailById(id: String): CarDetailReturnResult {
        val sourceAndId = id.split("-")
        val source = sourceAndId[1]
        val idInt = sourceAndId[0].toInt()

        if (GarageApplication.cache.containsKey(id)) {
            val entry = GarageApplication.cache[id]!!
            if (ChronoUnit.DAYS.between(LocalDate.now(), entry.first) <= 1) {
                return entry.second
            }
        }


        /*if (source == "8088") {
            return getService.getOne(
                "http://localhost:8088/details/${idInt}"
            ).apply { this.source = "8088" }.also {
                GarageApplication.cache.put(id, Pair(LocalDate.now(), it))
            }
        }*/

        val local = repository.findById(idInt)
        if (local.isPresent) {
            return CarDetailReturnResult(local.get().id!!,
                    local.get().price, local.get().name, local.get().description,
                    local.get().manufacturer, "local").also { GarageApplication.cache.put(id, Pair(LocalDate.now(), it))  }
        } else throw IllegalArgumentException("No detail with such ID")
    }

    /*
    override fun deleteDetail(id: Int) {
        try {
            repository.deleteById(id)
        } catch (e: Exception) {
            // nothing
        }
    }*/

    /*override fun addCarDetail(
       name: String, manufacturer: String,
       description: String, price: Double, type: String,
   ) {
       val detail = CarDetail.Builder()
           .setDetailCustomType(type)
           .setName(name)
           .setManufacturer(manufacturer)
           .setDescription(description)
           .setPrice(price)
           .get()

       repository.save(detail)
   }*/



}