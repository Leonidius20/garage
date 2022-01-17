package ua.leonidius.garage.mappers

import org.springframework.stereotype.Component
import ua.leonidius.garage.dto.CarDetailDto
import ua.leonidius.garage.model.CachedCarDetail
import ua.leonidius.garage.model.CachedCarDetailId
import ua.leonidius.garage.model.CarDetail

@Component
class CarDetailMapper {

    fun toDto(detail: CarDetail, source: String): CarDetailDto {
        return CarDetailDto(detail.id!!, detail.price,
            detail.name, detail.description, detail.manufacturer, detail.detailTypeCustomName, source, null)
    }

    fun fromDtoToCached(detailDto: CarDetailDto, fiveThousandPage: Int? = null): CachedCarDetail {
        return CachedCarDetail(
            CachedCarDetailId(detailDto.id!!, detailDto.source!!), detailDto.price,
            detailDto.name, detailDto.description, detailDto.manufacturer, fiveThousandPage)
    }

    fun toDto(cached: CachedCarDetail): CarDetailDto {
        return CarDetailDto(cached.embId.id, cached.price, cached.name,
            cached.description, cached.manufacturer!!, null, cached.embId.source, null)
    }

}