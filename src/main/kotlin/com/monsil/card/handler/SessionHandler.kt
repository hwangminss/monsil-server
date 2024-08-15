package com.monsil.card.handler

import com.monsil.card.config.MonSilLog
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitSession

interface SessionHandler {
    companion object : MonSilLog {
        val HTML
            get() = ServerResponse.ok().contentType(MediaType.TEXT_HTML)
    }

    suspend fun ServerRequest.userSession(): Map<String, Any?> {
        val session = this.awaitSession()
        return mapOf(
            "access" to (session.attributes["user"] as HashMap<*, *>)["access"],
            "userUid" to (session.attributes["user"] as HashMap<*, *>)["uid"],
        )
    }
}