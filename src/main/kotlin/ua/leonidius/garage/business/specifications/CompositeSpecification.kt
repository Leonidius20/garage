package ua.leonidius.garage.business.specifications

abstract class CompositeSpecification<T>: Specification<T> {

    override fun and(other: Specification<T>): Specification<T> {
        return AndSpecification(this, other)
    }

    override fun or(other: Specification<T>): Specification<T> {
        return OrSpecification(this, other)
    }

    override fun not(): Specification<T> {
        return NotSpecification(this)
    }

}