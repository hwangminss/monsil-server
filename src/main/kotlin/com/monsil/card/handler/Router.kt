package com.monsil.card.handler

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.api.GuestBookApiHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class Router(
    private val guestbookApiHandler: GuestBookApiHandler
) {
    companion object : MonSilLog

    @Bean
    fun routers() = coRouter {
        "api".nest {
            POST("/guestbook",guestbookApiHandler::add)
        }
    }
}