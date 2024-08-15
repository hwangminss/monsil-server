package com.monsil.card.repository.guestbook

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GuestBookRepository : ReactiveCrudRepository<GuestBookEntity, Long> {
    fun findAllByDeletedAtIsNullOrderByCreatedAtDesc(): Flux<GuestBookEntity>

    fun findById(id: Int): Mono<GuestBookEntity>

}


