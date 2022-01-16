package ua.leonidius.garage.service.car_detail.specifications.сoncrete

import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.service.car_detail.specifications.CompositeSpecification

class ManufacturerSpecification(private val manufacturer: String):
    CompositeSpecification<CarDetailDto>() {

    override fun isSatisfiedBy(entity: CarDetailDto): Boolean {
        return entity.manufacturer.equals(manufacturer, ignoreCase = true)
    }

}