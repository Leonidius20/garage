package ua.leonidius.garage.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.leonidius.garage.model.CachedCarDetail
import ua.leonidius.garage.model.CachedCarDetailId

@Repository
interface CachedCarDetailRepository: JpaRepository<CachedCarDetail, CachedCarDetailId> {
}