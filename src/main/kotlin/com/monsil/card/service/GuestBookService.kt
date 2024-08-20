package com.monsil.card.service

import com.moimpay.web.exception.CustomException
import com.moimpay.web.exception.ErrorCode
import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.GuestBookDTO
import com.monsil.card.handler.dto.GuestBookDeleteDTO
import com.monsil.card.handler.dto.GuestBookUpdateDTO
import com.monsil.card.repository.guestbook.GuestBookEntity
import com.monsil.card.repository.guestbook.GuestBookRepository
import com.monsil.card.util.PasswordEncoder
import com.monsil.card.util.PasswordEncoderImpl
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class GuestBookService(
    private val guestBookRepository: GuestBookRepository
) {
    companion object : MonSilLog

    suspend fun add(guestBookDTO: GuestBookDTO): GuestBookEntity {
        val addGB = GuestBookEntity.assign(guestBookDTO)
        return guestBookRepository.save(addGB).awaitSingle()
    }

    suspend fun list(): Flux<GuestBookEntity> {
        return guestBookRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc()
    }

    suspend fun update(gb: GuestBookUpdateDTO): GuestBookEntity {
        return guestBookRepository.findById(gb.id.toInt())
            .filter { it.deletedAt == null }
            .flatMap { entity ->

                if (gb.password.isNotEmpty()) {
                    val isMatch = PasswordEncoderImpl.matches(gb.password, entity.password)
                    if (!isMatch) {
                        throw CustomException(ErrorCode.NOT_EQUAL_PASSWORD)
                    }
                }

                entity.name = gb.name
                entity.detail = gb.detail

                guestBookRepository.save(entity)
            }.awaitSingle()
    }

    suspend fun delete(gb: GuestBookDeleteDTO): GuestBookEntity {
        return guestBookRepository.findById(gb.id.toInt())
            .filter { it.deletedAt == null }
            .flatMap { entity ->

                if (gb.password.isNotEmpty()) {
                    val isMatch = PasswordEncoderImpl.matches(gb.password, entity.password)
                    if (!isMatch) {
                        throw CustomException(ErrorCode.NOT_EQUAL_PASSWORD)
                    }
                }
                entity.deletedAt = LocalDateTime.now()

                guestBookRepository.save(entity)
            }.awaitSingle()    }

    suspend fun mono(id: Int): Mono<GuestBookEntity> {
        return guestBookRepository.findById(id)
    }
}
