package com.monsil.card.service

import com.moimpay.web.exception.CustomException
import com.moimpay.web.exception.ErrorCode
import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.FamilyDTO
import com.monsil.card.handler.dto.FamilyUpdateDTO
import com.monsil.card.handler.dto.GuestBookUpdateDTO
import com.monsil.card.repository.guestbook.FamilyEntity
import com.monsil.card.repository.guestbook.FamilyRepository
import com.monsil.card.repository.guestbook.GuestBookEntity
import com.monsil.card.util.PasswordEncoderImpl
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

    suspend fun update(family: FamilyUpdateDTO): FamilyEntity {
        return familyRepository.findById(family.id.toInt())
            .flatMap {
                it.bank = family.bank
                it.account = family.account
                it.kakao = family.kakao
                it.phone = family.phone
                it.message = family.message

                familyRepository.save(it)
            }.awaitSingle()
    }
}
