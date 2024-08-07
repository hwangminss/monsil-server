package com.monsil.card.repository.guestbook

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface GuestBookRepository : ReactiveCrudRepository<GuestBookEntity, Long> {

}


