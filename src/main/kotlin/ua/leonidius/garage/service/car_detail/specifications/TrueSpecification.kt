package ua.leonidius.garage.service.car_detail.specifications

class TrueSpecification<T>: CompositeSpecification<T>() {

    override fun isSatisfiedBy(entity: T) = true

}