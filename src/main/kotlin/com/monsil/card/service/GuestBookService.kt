package com.monsil.card.service

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.GuestBookDTO
import com.monsil.card.repository.guestbook.GuestBookEntity
import com.monsil.card.repository.guestbook.GuestBookRepository
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

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
        return guestBookRepository.findAll()
    }
}
