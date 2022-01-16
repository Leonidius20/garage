package ua.leonidius.garage.service.car_detail.specifications

internal class AndSpecification<T>(
    private val first: Specification<T>,
    private val second: Specification<T>
): CompositeSpecification<T>() {

    override fun isSatisfiedBy(entity: T): Boolean {
        return first.isSatisfiedBy(entity) && second.isSatisfiedBy(entity)
    }

}