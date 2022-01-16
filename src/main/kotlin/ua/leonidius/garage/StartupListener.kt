package ua.leonidius.garage

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import ua.leonidius.garage.service.car_detail.CarDetailServiceFacade


@Component
class StartupListener : ApplicationListener<ContextRefreshedEvent?> {

    @Autowired
    private lateinit var carDetailServiceFacade: CarDetailServiceFacade

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        // this is done so that the pages are cached
        carDetailServiceFacade.getAllDetails(1)
        carDetailServiceFacade.getAllDetails(2)
    }

}