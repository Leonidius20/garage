package ua.leonidius.garage.service.cached_car_detail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.mappers.CarDetailMapper
import ua.leonidius.garage.model.CachedCarDetail
import ua.leonidius.garage.model.CachedCarDetailId
import ua.leonidius.garage.repository.CachedCarDetailRepository
import java.util.*

@Service
class CachedCarDetailService {

    @Autowired
    private lateinit var repository: CachedCarDetailRepository

    @Autowired
    private lateinit var mapper: CarDetailMapper

    fun addToCache(details: List<CarDetailDto>) {
        repository.saveAll(details.map { mapper.fromDtoToCached(it) })
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 86400000)
    fun clearCache() {
        repository.deleteAll()
    }

    fun getFromCacheByIdAndSource(id: Int, source: String): Optional<CachedCarDetail> {
        return repository.findById(CachedCarDetailId(id, source))
    }

    fun searchByName(name: String): List<CachedCarDetail> {
        return repository.findByNameStartingWithIgnoreCase(name)
    }

    fun getPaged(page: Int): List<CachedCarDetail> {
        val result = repository.findAllByEmbId_Source("8088",
            PageRequest.of(page - 1, 5)).toMutableList()
        result.addAll(repository.findAllByEmbId_Source("8082",
            PageRequest.of(page - 1, 5)))
        return result
    }

    fun deleteFromCache(id: Int, source: String) {
        val embId = CachedCarDetailId(id, source)
        if (repository.existsById(embId))
            repository.deleteById(embId)
    }

    fun updateInCache(detailDto: CarDetailDto) {
        repository.save(mapper.fromDtoToCached(detailDto))
    }

}