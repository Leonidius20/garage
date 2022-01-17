package ua.leonidius.garage

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
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

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        cachedCarDetailService.clearCache()

        val numPages = carDetailServiceFacade.getNumberOfSlowPages()

        for (page in 1..numPages) {
            val results = runBlocking {
                getService
                    .get("${SLOW_SERVICE_URL}/price-list?page=$page")
                    .results.map { it.apply { source = "8088" } }
            }
            cachedCarDetailService.addToCache(results)
        }


        // this is done so that the pages are cached
        //carDetailServiceFacade.getAllDetails(1)
        //carDetailServiceFacade.getAllDetails(2)
    }

}