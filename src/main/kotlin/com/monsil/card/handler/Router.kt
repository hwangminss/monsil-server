package com.monsil.card.handler

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.api.FamilyApiHandler
import com.monsil.card.handler.api.GuestBookApiHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class Router(
    private val guestbookApiHandler: GuestBookApiHandler,
    private val familyApiHandler: FamilyApiHandler
) {
    companion object : MonSilLog

    @Bean
    fun routers() = coRouter {
        "api".nest {
            "guestbook".nest{
                POST("add", guestbookApiHandler::add)
                GET("list", guestbookApiHandler::list)
            }
            "family".nest{
                GET("add", familyApiHandler::add)
                GET("list", familyApiHandler::list)
            }
        }
    }
}