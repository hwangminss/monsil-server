package com.monsil.card.handler.api

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.GuestBookDTO
import com.monsil.card.service.GuestBookService
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class GuestBookApiHandler (
    private val guestBookService: GuestBookService
) {
    companion object : MonSilLog

    suspend fun add(request: ServerRequest): ServerResponse {
        val gb = request.bodyToMono(GuestBookDTO::class.java).awaitSingleOrNull()

        if (gb == null) {
            log.warn("GuestBookDTO is null")
            return ServerResponse.badRequest().bodyValueAndAwait("GuestBookDTO is missing or invalid")
        }

        return try {
            guestBookService.add(gb)
            log.info("GuestBookDTO added successfully: $gb")
            ServerResponse.ok().buildAndAwait()
        } catch (e: Exception) {
            log.error("Error adding GuestBookDTO to the service", e)
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValueAndAwait("Error adding guest book entry")
        }
    }

    suspend fun list(request: ServerRequest): ServerResponse {
        return try {
            val gblist = guestBookService.list().collectList().awaitSingleOrNull()
            if (gblist != null) {
                log.info("Successfully retrieved guest book list with ${gblist.size} entries")
                ServerResponse.ok().bodyValue(mapOf("result" to gblist, "status" to 200)).awaitSingle()
            } else {
                log.warn("Guest book list is empty or failed to retrieve")
                ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue(mapOf("status" to 204)).awaitSingle()
            }
        } catch (e: Exception) {
            log.error("Error retrieving guest book list", e)
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(mapOf("status" to 500, "error" to "Internal Server Error"))
                .awaitSingle()
        }
    }
}
