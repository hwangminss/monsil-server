package com.monsil.card.service

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.FamilyDTO
import com.monsil.card.repository.guestbook.FamilyEntity
import com.monsil.card.repository.guestbook.FamilyRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FamilyService(
    private val familyRepository: FamilyRepository
) {
    companion object : MonSilLog

    suspend fun add(familyDTO: FamilyDTO): FamilyEntity {
        val addGB = FamilyEntity.assign(familyDTO)
        return familyRepository.save(addGB).awaitSingle()
    }

    suspend fun list(): Flux<FamilyEntity> {
        return familyRepository.findAllByDeletedAtIsNullOrderById()
    }
}
