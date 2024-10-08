package com.monsil.card.handler

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.api.FamilyApiHandler
import com.monsil.card.handler.api.GuestBookApiHandler
import com.monsil.card.handler.api.ManageApiHandler
import com.monsil.card.handler.view.ViewHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class Router(
    private val viewHandler: ViewHandler,
    private val guestbookApiHandler: GuestBookApiHandler,
    private val familyApiHandler: FamilyApiHandler,
    private val manageApiHandler: ManageApiHandler
) {
    companion object : MonSilLog

    @Bean
    fun routers() = coRouter {
        "".nest{
            GET("/", viewHandler::index)
            GET("main", viewHandler::main)
            GET("login", viewHandler::login)
            GET("home", viewHandler::home)
            GET("family", viewHandler::family)
            GET("gb", viewHandler::guestbook)
            GET("ptMain", viewHandler::mainPhoto)
            GET("ptGallery", viewHandler::galleryPhoto)
        }
        "api".nest {
            "guestbook".nest{
                POST("add", guestbookApiHandler::add)
                GET("list", guestbookApiHandler::list)
                POST("update", guestbookApiHandler::update)
                POST("delete", guestbookApiHandler::delete)
                POST("maDelete", guestbookApiHandler::maDelete)
                GET("mono/{id}", guestbookApiHandler::mono)
            }
            "family".nest{
                POST("add", familyApiHandler::add)
                GET("list", familyApiHandler::list)
                POST("update", familyApiHandler::update)
                DELETE("delete/{id}", familyApiHandler::delete)
            }
            "manager".nest{
                POST("signUp", manageApiHandler::signUp)
                POST("login", manageApiHandler::login)
                POST("uploadMain", manageApiHandler::uploadMainPhoto)
                POST("modifyMain", manageApiHandler::modifyMainPhoto)
                POST("uploadGallery", manageApiHandler::uploadGalleryPhotos)
                DELETE("deleteGallery/{id}", manageApiHandler::deleteGalleryPhotos)
            }
        }
    }
}