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
import java.time.LocalDateTime

@Component
class GuestBookApiHandler (
    private val guestBookService: GuestBookService
) {
    companion object : MonSilLog

    suspend fun add(request: ServerRequest): ServerResponse {
        val gb = request.bodyToMono(GuestBookDTO::class.java).awaitSingleOrNull()

        if (gb == null) {
            log.warn("GuestBookDTO가 null입니다")
            return ServerResponse.badRequest().bodyValueAndAwait("GuestBookDTO가 없거나 유효하지 않습니다")
        }

        return try {
            val result = guestBookService.add(gb)
            log.info("GuestBookDTO가 성공적으로 추가되었습니다: $gb")
            ServerResponse.ok().bodyValueAndAwait(result)
        } catch (e: Exception) {
            log.error("서비스에 GuestBookDTO를 추가하는 중 오류 발생", e)
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValueAndAwait("방명록 항목 추가 중 오류 발생")
        }
    }

    suspend fun list(request: ServerRequest): ServerResponse {
        return try {
            val gblist = guestBookService.list().collectList().awaitSingleOrNull()
            if (gblist != null) {
                log.info("성공적으로 방명록 목록을 가져왔습니다. 항목 수: ${gblist.size}")
                ServerResponse.ok().bodyValue(mapOf("result" to gblist, "status" to 200)).awaitSingle()
            } else {
                log.warn("방명록 목록이 비어있거나 가져오는 데 실패했습니다")
                ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue(mapOf("status" to 204)).awaitSingle()
            }
        } catch (e: Exception) {
            log.error("방명록 목록을 가져오는 중 오류 발생", e)
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(mapOf("status" to 500, "error" to "내부 서버 오류"))
                .awaitSingle()
        }
    }
}
