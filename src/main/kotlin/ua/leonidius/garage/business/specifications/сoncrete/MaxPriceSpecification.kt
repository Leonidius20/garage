package ua.leonidius.garage.business.specifications.сoncrete

import ua.leonidius.garage.business.specifications.CompositeSpecification
import ua.leonidius.garage.presentation.results.CarDetailReturnResult

class MaxPriceSpecification(private val maxPrice: Double): CompositeSpecification<CarDetailReturnResult>() {

    override fun isSatisfiedBy(entity: CarDetailReturnResult): Boolean {
        return entity.price <= maxPrice
    }

}