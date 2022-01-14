package ua.leonidius.garage.mappers

import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.model.CarDetail

@Component
class CarDetailMapper {

    fun toDto(detail: CarDetail, source: String): CarDetailDto {
        return CarDetailDto(detail.id!!, detail.price,
            detail.name, detail.description, detail.manufacturer, detail.detailTypeCustomName, source)
    }

}