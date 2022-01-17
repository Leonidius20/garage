package ua.leonidius.garage.service.car_detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.exception.InvalidIdException
import ua.leonidius.garage.mappers.CarDetailMapper
import ua.leonidius.garage.model.CarDetail
import ua.leonidius.garage.repository.CarDetailRepository
import ua.leonidius.garage.service.cached_car_detail.CachedCarDetailService
import ua.leonidius.garage.service.car_detail.network.GetService
import ua.leonidius.garage.service.car_detail.specifications.Specification
import ua.leonidius.garage.service.car_detail.specifications.TrueSpecification
import ua.leonidius.garage.service.car_detail.specifications.сoncrete.ManufacturerSpecification
import ua.leonidius.garage.service.car_detail.specifications.сoncrete.MaxPriceSpecification

@Service
class CarDetailServiceFacadeImpl : CarDetailServiceFacade {

    @Autowired
    private lateinit var repository: CarDetailRepository

    @Autowired
    private lateinit var cacheService: CachedCarDetailService

    @Autowired
    private lateinit var detailMapper: CarDetailMapper

    private val getService = GetService()

    private val SLOW_SERVICE_URL = "http://localhost:8088"
    private val FIVE_THOUSAND_SERVICE_URL = "http://localhost:8082"

    override fun getAllDetails(page: Int): Collection<CarDetailDto>  {
        val results = java.util.Collections.synchronizedList(mutableListOf<CarDetailDto>())

        runBlocking {
            val tasks = listOf(

                launch(Dispatchers.IO) {
                    // local results
                    results.addAll(repository.findAll(PageRequest.of(page, 5)).map {
                        detailMapper.toDto(it, "local")
                    })
                },
                launch(Dispatchers.IO) {
                    results.addAll(cacheService.getPaged(page).map { detailMapper.toDto(it) })
                }
            )

            tasks.joinAll()
        }

        return  results
    }

    override fun findDetailsByNameWithFilter(
        name: String,
        maxPrice: Float?, minPrice: Float?, manufacturer: String?
    ): List<CarDetailDto> {
        val results = java.util.Collections.synchronizedList(mutableListOf<CarDetailDto>())

        runBlocking {
            val tasks = listOf(
                launch(Dispatchers.IO) {
                    results.addAll(repository.findByNameContainingIgnoreCase(name).map {
                        detailMapper.toDto(it, "local")
                    })
                },
                launch(Dispatchers.IO) {
                    results.addAll(cacheService.searchByName(name).map {
                        detailMapper.toDto(it)
                    })
                },
            )
            tasks.joinAll()
        }

        val filter = createFilter(maxPrice, minPrice, manufacturer)
        results.retainAll { filter.isSatisfiedBy(it) }

        return results
    }


    override fun getLocalDetailById(id: Int): CarDetailDto? {
        if (id < 0)
            return null
        return getDetailById("${id}-local")
    }

    override fun getDetailById(id: String): CarDetailDto? {
        val sourceAndId = id.split("-")
        val source = sourceAndId[1]
        val idInt = sourceAndId[0].toInt()

        if (idInt < 0) return null
        if (source != "local" && source != "8088" && source != "8082") return null

        if (source == "8088" || source == "8082") {
            val optional = cacheService.getFromCacheByIdAndSource(idInt, source)
            if (optional.isEmpty) return null
            return detailMapper.toDto(optional.get())
        } else if (source == "local") {
            val local = repository.findById(idInt)
            if (local.isPresent) {
                return detailMapper.toDto(local.get(), "local")
            } else return null // doesn't exist
        } else throw IllegalArgumentException("Invalid data source $source")
    }


    override fun deleteDetail(id: Int, source: String): Boolean {
        if (source == "local") {
            if (repository.existsById(id)) {
                repository.deleteById(id)
                return true
            } else return false
            // else throw InvalidIdException("No item with such an ID exists")
        } else if (source == "8088") {
            throw InvalidIdException("Source $source doesn't support deleting details")
        } else if (source == "8082") {
            if (getService.delete("$FIVE_THOUSAND_SERVICE_URL/delete/$id")) {
                cacheService.deleteFromCache(id, source)
                return true
            }
            else return false
        } else throw InvalidIdException("Source $source does not exist")
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

    override fun updateDetail(id: Int, source: String, name: String?, manufacturer: String?, description: String?,
                                   type: String?, price: Float?): CarDetailDto? {
        if (!validateId(id) || !validateSource(source)) return null

        if (source == "local") {
            val local = repository.findById(id)
            if (local.isEmpty) return null
            val detail = local.get()

            name?.also { detail.name = it }
            manufacturer?.also { detail.manufacturer = it }
            description?.also { detail.description = it }
            type?.also { detail.detailTypeCustomName = it }
            price?.also { detail.price = it.toDouble() }

            return detailMapper.toDto(repository.save(detail), "local")
        } else if (source == "8088") {
            throw InvalidIdException("Source $source doesn't support updating details")
        } else if (source == "8082") {
            val params = mutableMapOf<String, Any>()
            var urlTemplate = UriComponentsBuilder.fromHttpUrl(
                "$FIVE_THOUSAND_SERVICE_URL/modify/$id"
            )

            name?.also {
                params["name"] = it
                urlTemplate = urlTemplate.queryParam("name", it)
            }
            manufacturer?.also {
                params["manufacturer"] = it
                urlTemplate = urlTemplate.queryParam("manufacturer", it)
            }
            description?.also {
                params["description"] = it
                urlTemplate = urlTemplate.queryParam("description", it)
            }
            type?.also {
                params["type"] = it
                urlTemplate = urlTemplate.queryParam("type", it)
            }
            price?.also {
                params["price"] = it
                urlTemplate = urlTemplate.queryParam("price", it)
            }

            println(params.toString())

            val result = runBlocking { getService.getOneWithParams(
                urlTemplate.encode().toUriString(),
                params
            ) }
            if (result == null) return null

            cacheService.updateInCache(result.apply { this.source = "8082" })

            return result
        } else throw InvalidIdException("Source $source doesn't exist")
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

    override fun carDetailExists(id: Int, source: String): Boolean {
        if (source == "local") {
            return repository.existsById(id)
        } else {
            try {
                val detail = getDetailById("$id-$source")
                return detail != null
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
    }

    override fun getNumberOfSlowPages(): Int {
        return runBlocking {
            getService.getInteger(SLOW_SERVICE_URL + "/num-pages")
        }
    }

    override fun getNumberOfFiveThousandPages(): Int {
        return runBlocking {
            getService.getInteger(FIVE_THOUSAND_SERVICE_URL + "/num-pages")
        }
    }

    private fun validateSource(source: String): Boolean {
        if (source != "local" && source != "8088" && source != "8082")
            return false
        return true
            // throw InvalidIdException("Source with name $source does not exist")
    }

    private fun validateId(id: Int): Boolean {
        if (id < 0) return false
        return true
    // throw InvalidIdException("ID cannot be a negative number")
    }

}