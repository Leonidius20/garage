package ua.leonidius.garage.service.cached_car_detail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.mappers.CarDetailMapper
import ua.leonidius.garage.repository.CachedCarDetailRepository

@Service
class CachedCarDetailService {

    @Autowired
    private lateinit var repository: CachedCarDetailRepository

    @Autowired
    private lateinit var mapper: CarDetailMapper

    fun addToCache(details: List<CarDetailDto>) {
        repository.saveAll(details.map { mapper.fromDtoToCached(it) })
    }

    fun clearCache() {
        repository.deleteAll()
    }

    fun getFromCacheByIdAndSource(id: Int, source: String) {

    }

}