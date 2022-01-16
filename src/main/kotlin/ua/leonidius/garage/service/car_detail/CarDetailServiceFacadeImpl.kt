package ua.leonidius.garage.service.car_detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ua.leonidius.garage.GarageApplication
import ua.leonidius.garage.service.car_detail.network.GetService
import ua.leonidius.garage.repository.CarDetailRepository
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.dto.SearchReturnResult
import ua.leonidius.garage.mappers.CarDetailMapper
import ua.leonidius.garage.model.CarDetail
import ua.leonidius.garage.service.car_detail.specifications.Specification
import ua.leonidius.garage.service.car_detail.specifications.TrueSpecification
import ua.leonidius.garage.service.car_detail.specifications.сoncrete.ManufacturerSpecification
import ua.leonidius.garage.service.car_detail.specifications.сoncrete.MaxPriceSpecification
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class CarDetailServiceFacadeImpl : CarDetailServiceFacade {

    @Autowired
    private lateinit var repository: CarDetailRepository

    @Autowired
    private lateinit var detailMapper: CarDetailMapper

    private val getService = GetService()

    private val SLOW_SERVICE_URL = "http://localhost:8088"

    override fun getAllDetails(page: Int): Collection<CarDetailDto>  {

        if (GarageApplication.pageCache.containsKey(page)) {
            val entry = GarageApplication.pageCache[page]!!
            if (ChronoUnit.DAYS.between(LocalDate.now(), entry.first) <= 1) {
                return entry.second
            }
        }

        val results = java.util.Collections.synchronizedList(mutableListOf<CarDetailDto>())

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
                        detailMapper.toDto(it, "local")
                    })
                },
                launch(Dispatchers.IO) {
                    // results from 5000 service
                    results.addAll(getService.get(
                        "http://localhost:8082/details?page=${page % 500}"
                    ).results.toTypedArray().slice(page*10..page*10+10).map { it.apply { source = "8082" } })
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

    /**
     * returns details directly without using cache
     */
    override fun findDetailsByNameWithFilter(
        name: String,
        maxPrice: Float?, minPrice: Float?, manufacturer: String?
    ): List<CarDetailDto> {
        val results = java.util.Collections.synchronizedList(mutableListOf<CarDetailDto>())

        runBlocking {
            val tasks = listOf(
                launch(Dispatchers.IO) {
                    results.addAll(getService.get( // slow service
                        "${SLOW_SERVICE_URL}/search?query=$name"
                    ).results.map { it.apply { source = "8088" } })
                },
                launch(Dispatchers.IO) {
                    results.addAll(repository.findByNameContainingIgnoreCase(name).map {
                        detailMapper.toDto(it, "local")
                    })
                },
            )
            tasks.joinAll()
        }

        GarageApplication.cache.putAll(
            results.map { Pair(LocalDate.now(), it) }
                .associateBy { "${it.second.id}-${it.second.source}" })

        val filter = createFilter(maxPrice, minPrice, manufacturer)
        results.retainAll { filter.isSatisfiedBy(it) }

        // GarageApplication.searchCache[name] = Pair(LocalDate.now(), results)

        return results
    }

    /**
     * returns details only from cache
     */
    override fun findDetailsCached(
        name: String,
        maxPrice: Float?, minPrice: Float?, manufacturer: String?
    ): List<CarDetailDto> {
        val query = name.trim()
        val results = GarageApplication.cache.filter {
            it.value.second.name.startsWith(query, ignoreCase = true) }
            .map { it.value.second }.toMutableList()

        val filter = createFilter(maxPrice, minPrice, manufacturer)
        results.retainAll { filter.isSatisfiedBy(it) }

        return results
    }

    override fun getLocalDetailById(id: Int): CarDetailDto? {
        return getDetailById("${id}-local")
    }

    override fun getDetailById(id: String): CarDetailDto? {
        val sourceAndId = id.split("-")
        val source = sourceAndId[1]
        val idInt = sourceAndId[0].toInt()

        if (GarageApplication.cache.containsKey(id)) {
            val entry = GarageApplication.cache[id]!!
            if (ChronoUnit.DAYS.between(LocalDate.now(), entry.first) <= 1) {
                return entry.second
            }
        }

        if (source == "8088") {
            return runBlocking {
                return@runBlocking getService.getOne(
                    "${SLOW_SERVICE_URL}/details/${idInt}"
                ).apply { this.source = "8088" }.also {
                    GarageApplication.cache.put(id, Pair(LocalDate.now(), it))
                }
            }
        } else if (source == "local") {
            val local = repository.findById(idInt)
            if (local.isPresent) {
                return detailMapper.toDto(local.get(), "local").also {
                        GarageApplication.cache.put(id, Pair(LocalDate.now(), it))
                    }
            } else return null // doesn't exist
        } else throw IllegalArgumentException("Invalid data source ${source}")
    }


    override fun deleteDetail(id: Int) {
        repository.deleteById(id)
    }

    override fun addCarDetail(
       name: String, manufacturer: String,
       description: String, price: Float, type: String,
   ): CarDetailDto {
       val detail = CarDetail.Builder()
           .setDetailCustomType(type)
           .setName(name)
           .setManufacturer(manufacturer)
           .setDescription(description)
           .setPrice(price.toDouble())
           .get()

       return detailMapper.toDto(repository.save(detail), "local")
   }

    override fun updateLocalDetail(id: Int, name: String?, manufacturer: String?, description: String?,
                                   type: String?, price: Float?): CarDetailDto? {

        val local = repository.findById(id)
        if (local.isEmpty) return null
        val detail = local.get()


        if (name != null) detail.name = name
        if (manufacturer != null) detail.manufacturer = manufacturer
        if (description != null) detail.description = description
        if (type != null) detail.detailTypeCustomName = type
        if (price != null) detail.price = price.toDouble()

        // removing from cache
        if (GarageApplication.cache.containsKey("${id}-local")) {
            GarageApplication.cache.remove("${id}-local")
        }

        return detailMapper.toDto(repository.save(detail), "local")
    }

    private fun createFilter(maxPrice: Float?, minPrice: Float?, manufacturer: String?): Specification<CarDetailDto> {
        var filter: Specification<CarDetailDto> = TrueSpecification()

        if (maxPrice != null)
            filter = filter.and(MaxPriceSpecification(maxPrice.toDouble()))

        if (minPrice != null)
            filter = filter.and(MaxPriceSpecification(minPrice.toDouble()).not())

        if (manufacturer != null)
            filter = filter.and(ManufacturerSpecification(manufacturer))

        return filter
    }

}