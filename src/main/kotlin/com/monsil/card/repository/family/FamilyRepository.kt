package com.monsil.card.repository.family

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface FamilyRepository : ReactiveCrudRepository<FamilyEntity, Long> {
    fun findAllByDeletedAtIsNullOrderById(): Flux<FamilyEntity>

    fun findById(id: Int): Mono<FamilyEntity>

}


