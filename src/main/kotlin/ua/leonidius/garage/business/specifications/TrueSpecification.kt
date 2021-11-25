package ua.leonidius.garage.business.specifications

class TrueSpecification<T>: CompositeSpecification<T>() {

    override fun isSatisfiedBy(entity: T) = true

}