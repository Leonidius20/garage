package ua.leonidius.garage.service.car_detail.specifications.сoncrete

import ua.leonidius.garage.service.car_detail.specifications.CompositeSpecification
import ua.leonidius.garage.dto.CarDetailDto

class MaxPriceSpecification(private val maxPrice: Double): CompositeSpecification<CarDetailDto>() {

    override fun isSatisfiedBy(entity: CarDetailDto): Boolean {
        return entity.price <= maxPrice
    }

}