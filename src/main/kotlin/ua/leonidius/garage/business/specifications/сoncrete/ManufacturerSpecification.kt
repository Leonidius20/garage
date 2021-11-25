package ua.leonidius.garage.business.specifications.сoncrete

import ua.leonidius.garage.presentation.results.CarDetailReturnResult
import ua.leonidius.garage.business.specifications.CompositeSpecification

class ManufacturerSpecification(private val manufacturer: String):
    CompositeSpecification<CarDetailReturnResult>() {

    override fun isSatisfiedBy(entity: CarDetailReturnResult): Boolean {
        return entity.manufacturer.equals(manufacturer, ignoreCase = true)
    }

}