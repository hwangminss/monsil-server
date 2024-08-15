package com.monsil.card.handler

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.api.FamilyApiHandler
import com.monsil.card.handler.api.GuestBookApiHandler
import com.monsil.card.handler.view.ViewHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class Router(
    private val viewHandler: ViewHandler,
    private val guestbookApiHandler: GuestBookApiHandler,
    private val familyApiHandler: FamilyApiHandler
) {
    companion object : MonSilLog

    @Bean
    fun routers() = coRouter {
        "".nest{
            "/".nest{
                GET("/", viewHandler::index)
            }
        }
        "api".nest {
            "guestbook".nest{
                POST("add", guestbookApiHandler::add)
                GET("list", guestbookApiHandler::list)
                POST("update", guestbookApiHandler::update)
                POST("delete", guestbookApiHandler::delete)
                GET("mono/{id}", guestbookApiHandler::mono)
            }
            "family".nest{
                GET("add", familyApiHandler::add)
                GET("list", familyApiHandler::list)
            }
        }
    }
}