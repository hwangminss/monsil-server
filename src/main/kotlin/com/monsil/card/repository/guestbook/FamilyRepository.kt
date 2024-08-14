package com.monsil.card.repository.guestbook

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface FamilyRepository : ReactiveCrudRepository<FamilyEntity, Long> {
    fun findAllByDeletedAtIsNullOrderById(): Flux<FamilyEntity>
}


