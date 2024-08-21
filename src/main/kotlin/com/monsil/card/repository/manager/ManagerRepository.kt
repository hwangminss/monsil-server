package com.monsil.card.repository.manager

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface ManagerRepository : ReactiveCrudRepository<ManagerEntity, Long> {
    fun findByNameAndDeletedAtNull(name: String): Mono<ManagerEntity>
    fun findByUid(uid: String): Mono<ManagerEntity>
}



