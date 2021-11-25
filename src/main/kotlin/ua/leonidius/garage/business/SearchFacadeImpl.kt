package ua.leonidius.garage.business

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.leonidius.garage.data.car_details.CarDetail
import ua.leonidius.garage.data.car_details.CarDetailRepository
import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.presentation.results.SearchReturnResult
import ua.leonidius.garage.business.request_handlers.IllegalCharsValidator
import ua.leonidius.garage.business.request_handlers.MinLengthValidator
import ua.leonidius.garage.business.request_handlers.NotEmptyValidator
import ua.leonidius.garage.business.request_handlers.SearchHandler
import ua.leonidius.garage.business.specifications.Specification

@Service
class SearchFacadeImpl : SearchFacade {

    @Autowired
    private lateinit var repository: CarDetailRepository

    override fun getAllDetails(): SearchReturnResult {
        val results = mutableListOf<CarDetail>()

        results.addAll(repository.findAll())

        // TODO: search in other 2 services

        return SearchReturnResult(results.map {
            CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer)
        }.toMutableList())
    }

    override fun findDetailsByNameWithFilter(
        name: String, filter: Specification<CarDetailReturnResult>
    ): SearchReturnResult {

        val notEmptyValidator = NotEmptyValidator()
        val minLengthValidator = MinLengthValidator(3)
        val illegalCharsValidator = IllegalCharsValidator()
        val searchHandler = SearchHandler(repository)

        notEmptyValidator.setNext(minLengthValidator)
        minLengthValidator.setNext(illegalCharsValidator)
        illegalCharsValidator.setNext(searchHandler)

        val results = notEmptyValidator.handleSearchQuery(name)

        results.results.retainAll { filter.isSatisfiedBy(it) }
        return results
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

    override fun getDetailById(id: Int): CarDetailReturnResult {
        if (id < 1)
            throw IllegalArgumentException("Detail ID cannot be smaller than 1")

        val results = mutableListOf<CarDetail>()

        results.add(repository.getById(id))

        // TODO: 2 more services

        results.retainAll { it.id == id }

        if (results.isEmpty())
            throw IllegalArgumentException("No detail with such ID")

        return results.map {
            CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer)
        }[0]
    }

}