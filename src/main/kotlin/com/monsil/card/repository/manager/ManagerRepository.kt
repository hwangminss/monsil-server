package com.monsil.card.repository.manager

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface ManagerRepository : ReactiveCrudRepository<ManagerEntity, Long> {
    suspend fun findUserByNameAndDeletedAtNull(name: String): Mono<ManagerEntity>
}



