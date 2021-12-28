package ua.leonidius.garage.business

import de.qaware.tools.collectioncacheableforspring.CollectionCacheable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.CrossOrigin
import ua.leonidius.garage.GarageApplication
import ua.leonidius.garage.business.network.GetService
import ua.leonidius.garage.data.car_details.CarDetail
import ua.leonidius.garage.data.car_details.CarDetailRepository
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult

@Service
class SearchFacadeImpl : SearchFacade {

    @Autowired
    private lateinit var repository: CarDetailRepository

    private val getService = GetService()

    private var currentPage = 0

    override fun getAllDetails(page: Int): Collection<CarDetailReturnResult> {
        currentPage = page
        return getAllDetails().values
    }

    // @CollectionCacheable(cacheNames = ["details"])
    fun getAllDetails(): Map<String, CarDetailReturnResult> {

        if (GarageApplication.pageCache.containsKey(currentPage)) {
            return GarageApplication.pageCache.get(currentPage)!!
        }


        val results = mutableMapOf<String, CarDetailReturnResult>()

        results.putAll ( // slow service
            getService.get(
                "http://localhost:8088/price-list?page=$currentPage"
            ).get().results.associate { Pair("${it.id}-8088", it.apply { source = "8088" }) }
                .also { GarageApplication.cache.putAll(it) }
        )


        val res = repository.findAll(PageRequest.of(currentPage, 5)).map {
            CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer, "local")
        }.associateBy { "${it.id}-local" }
            .also { GarageApplication.cache.putAll(it) }


        results.putAll( // 5000 service
            getService.get(
                "http://localhost:8082/details?page=${currentPage % 500}" // TODO: get only 10 results
            ).get().results.associate { Pair("${it.id}-8082", it.apply { source = "8082" }) }
        )

        GarageApplication.pageCache.put(currentPage, results)

        return  results
    }

    @Cacheable("results")
    override fun findDetailsByNameWithFilter(
        name: String
    ): SearchReturnResult {

        if (GarageApplication.searchCache.containsKey(name)) {
            return SearchReturnResult(GarageApplication.searchCache[name]!!.values)
        }

        val results = mutableMapOf<String, CarDetailReturnResult>()

        results.putAll ( // slow service
            getService.get(
                "http://localhost:8088/search?query=$name"
            ).get().results.associate { Pair("${it.id}-8088", it.apply { source = "8088" }) }
                .also { GarageApplication.cache.putAll(it) }
        )

        results.putAll(repository.findAll(PageRequest.of(currentPage, 5)).map {
            CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer, "local")
        }.associateBy { "${it.id}-local" }
            .also { GarageApplication.cache.putAll(it) })

        GarageApplication.searchCache[name] = results


        return SearchReturnResult(results.values)
    }

    override fun addCarDetail(
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
    }

    // @Cacheable(cacheNames = ["details"])
    override fun getDetailById(id: String): CarDetailReturnResult {
        val sourceAndId = id.split("-")
        val source = sourceAndId[1]
        val idInt = sourceAndId[0].toInt()

        if (GarageApplication.cache.containsKey(id)) {
            return GarageApplication.cache.get(id)!!
        }

        // TODO: ckeck the right source
        if (source == "8088") {
            return getService.getOne(
                "http://localhost:8088/details/${idInt}"
            )
        }

        /*if (source == "8082") {
            return getService.getOne(
                "http://localhost:8082/details/${idInt}"
            )
        }*/

        val results = mutableListOf<CarDetailReturnResult>()

        val local = repository.findById(idInt)
        if (local.isPresent) {
            results.add(
                CarDetailReturnResult(local.get().id!!,
                    local.get().price, local.get().name, local.get().description,
                    local.get().manufacturer, "local")
            )
        }

        results.retainAll { it.id == idInt }

        if (results.isEmpty())
            throw IllegalArgumentException("No detail with such ID")

        return results[0]
    }

    @CacheEvict("results", allEntries = true)
    override fun deleteDetail(id: Int) {
        try {
            repository.deleteById(id)
        } catch (e: Exception) {
            // nothing
        }
    }

}