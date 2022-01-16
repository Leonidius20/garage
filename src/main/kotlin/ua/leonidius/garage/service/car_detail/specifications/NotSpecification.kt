package ua.leonidius.garage.service.car_detail.specifications

class NotSpecification<T>(
    private val wrapped: Specification<T>
): CompositeSpecification<T>() {

    override fun isSatisfiedBy(entity: T): Boolean {
        return !wrapped.isSatisfiedBy(entity)
    }

}