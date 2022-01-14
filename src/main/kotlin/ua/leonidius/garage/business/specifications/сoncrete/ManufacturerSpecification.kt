package ua.leonidius.garage.business.specifications.сoncrete

import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.business.specifications.CompositeSpecification

class ManufacturerSpecification(private val manufacturer: String):
    CompositeSpecification<CarDetailDto>() {

    override fun isSatisfiedBy(entity: CarDetailDto): Boolean {
        return entity.manufacturer.equals(manufacturer, ignoreCase = true)
    }

}