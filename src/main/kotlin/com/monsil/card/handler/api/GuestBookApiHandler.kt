package com.monsil.card.handler.api

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.GuestBookDTO
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class GuestBookApiHandler (

) {
    private val log = LoggerFactory.getLogger(GuestBookApiHandler::class.java)

    companion object : MonSilLog {
        val BAD = ServerResponse.badRequest().build()
    }

    suspend fun add(request: ServerRequest): ServerResponse {
        val gb = request.bodyToMono(GuestBookDTO::class.java).awaitSingleOrNull() ?: return BAD.awaitSingle()

        return ServerResponse.ok().build().awaitSingle()
    }
}