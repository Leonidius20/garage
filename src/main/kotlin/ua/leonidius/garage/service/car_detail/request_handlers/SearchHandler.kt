package ua.leonidius.garage.service.car_detail.request_handlers

/*class SearchHandler(private val repository: CarDetailRepository,
                    private val getService: GetService): BaseHandler() {

    override fun handleSearchQuery(query: String): SearchReturnResult {
        val resultSet = repository.findByNameContainingIgnoreCase(query).map {
            CarDetailReturnResult(it.id!!, it.price, it.name, it.description, it.manufacturer)
        }.toMutableList()

        resultSet.addAll(
            getService.get(
                "https://boiling-bastion-00760.herokuapp.com/details/search?query=$query"
            ).results
        )

        resultSet.addAll(
            getService.get(
                "https://powerful-cliffs-34452.herokuapp.com/price-list"
            ).results.filter { query.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(it.name) }
        )

        return getNext()?.handleSearchQuery(query) ?: SearchReturnResult(resultSet)
    }

}*/