package ua.leonidius.garage.business.specifications

internal class OrSpecification<T>(
    private val first: Specification<T>,
    private val second: Specification<T>
): CompositeSpecification<T>() {

    override fun isSatisfiedBy(entity: T): Boolean {
        return first.isSatisfiedBy(entity) || second.isSatisfiedBy(entity)
    }

}