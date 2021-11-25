package ua.leonidius.garage.business.specifications

interface Specification<T> {

    fun isSatisfiedBy(entity: T): Boolean

    fun and(other: Specification<T>): Specification<T>

    fun or(other: Specification<T>): Specification<T>

    fun not(): Specification<T>

}