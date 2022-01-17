package ua.leonidius.garage.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "cached_car_detail")
class CachedCarDetail(

    @EmbeddedId
    var embId: CachedCarDetailId,

    val price: Double,

    val name: String,

    val description: String?,

    val manufacturer: String?,

    val fiveThousandPage: Int? = null,


)

@Embeddable
data class CachedCarDetailId(
    var id: Int,
    var source: String,

): Serializable