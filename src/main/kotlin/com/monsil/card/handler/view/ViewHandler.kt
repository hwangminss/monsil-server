package com.monsil.card.handler.view


import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.SessionHandler
import com.monsil.card.handler.SessionHandler.Companion.HTML
import com.monsil.card.service.FamilyService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class ViewHandler(
    private val familyService: FamilyService
) : SessionHandler {
    companion object : MonSilLog

    suspend fun index(request: ServerRequest): ServerResponse {
        return HTML.render(
            "index/index",
            mapOf("userList" to familyService.list().collectList()
            ),
        ).awaitSingle()
    }
}