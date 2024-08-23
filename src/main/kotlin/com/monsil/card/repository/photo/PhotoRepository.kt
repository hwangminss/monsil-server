package com.monsil.card.repository.photo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PhotoRepository : ReactiveCrudRepository<PhotoEntity, Long> {
    fun findByIdAndIsMain(id: Long,isMain: Int): Mono<PhotoEntity>

    fun findByIsMain(isMain: Int): Flux<PhotoEntity>
}


