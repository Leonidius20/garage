package ua.leonidius.garage

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ua.leonidius.garage.service.cached_car_detail.CachedCarDetailService
import ua.leonidius.garage.service.car_detail.CarDetailServiceFacade
import ua.leonidius.garage.service.car_detail.network.GetService


@Component
class StartupListener : ApplicationListener<ContextRefreshedEvent?> {

    @Autowired
    private lateinit var carDetailServiceFacade: CarDetailServiceFacade

    @Autowired
    private lateinit var cachedCarDetailService: CachedCarDetailService

    private val getService = GetService()

    private val SLOW_SERVICE_URL = "http://localhost:8088"

    private val FIVE_THOUSAND_SERVICE_URL = "http://localhost:8082"

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        clearCacheAndLoad()
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 86400000) // fixedDelay = 24h
    fun clearCacheAndLoad() {
        println("Started loading data to cache...")

        cachedCarDetailService.clearCache()

        val numSlowPages = carDetailServiceFacade.getNumberOfSlowPages()

        for (page in 1..numSlowPages + 1) {
            val results = runBlocking {
                getService
                    .get("${SLOW_SERVICE_URL}/price-list?page=$page")
                    .results.map { it.apply { source = "8088" } }
            }
            cachedCarDetailService.addToCache(results)
        }

        val num5kPages = carDetailServiceFacade.getNumberOfFiveThousandPages()
        for (page in 1..num5kPages + 1) {
            val results = runBlocking {
                getService
                    .get("${FIVE_THOUSAND_SERVICE_URL}/details?page=$page")
                    .results.map { it.apply { source = "8082" } }
            }
            cachedCarDetailService.addToCache(results)
        }

        println("Done loading data to cache.")

    }

}