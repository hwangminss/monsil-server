package com.monsil.card.repository.photo

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PhotoRepository : ReactiveCrudRepository<PhotoEntity, Long> {

}


