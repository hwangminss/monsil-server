package com.monsil.card.repository.photo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PhotoRepository : ReactiveCrudRepository<PhotoEntity, Long> {
    fun findByIsMain(isMain: Int): Mono<PhotoEntity>
}


