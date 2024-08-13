package com.monsil.card.repository.guestbook

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface GuestBookRepository : ReactiveCrudRepository<GuestBookEntity, Long> {
    fun findAllByDeletedAtIsNullOrderByCreatedAtDesc(): Flux<GuestBookEntity>

}


