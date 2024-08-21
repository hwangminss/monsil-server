package com.monsil.card.handler.api

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.LoginDTO
import com.monsil.card.handler.dto.SignUpDTO
import com.monsil.card.service.ManageService
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.ArrayList
import java.util.HashMap

@Component
class ManageApiHandler (
    private val manageService: ManageService
) {
    companion object : MonSilLog {
        val BAD = ServerResponse.badRequest().build()
    }

    suspend fun signUp(request: ServerRequest): ServerResponse {
        val dto = request.bodyToMono(SignUpDTO::class.java).awaitSingleOrNull()
            ?: return ServerResponse.badRequest().bodyValueAndAwait("SignUpDTO 없거나 유효하지 않습니다")
        val result = manageService.signUp(dto).awaitSingle()

        return ServerResponse.ok().bodyValueAndAwait(result)
    }

    suspend fun login(request: ServerRequest): ServerResponse {
        val login = request.bodyToMono(LoginDTO::class.java).awaitSingleOrNull() ?: return BAD.awaitSingle()
        val user = manageService.login(login)
        print("$user useruser")

        request.session().doOnNext { session ->
            session.attributes["user"] = HashMap<String, Any>().apply {
                put("uid", user.uid)
                put("name", user.name)
            }
            session.attributes[HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY] =
                SecurityContextHolder.createEmptyContext().apply {
                    authentication = UsernamePasswordAuthenticationToken(
                        user.uid,
                        null,
                        ArrayList<GrantedAuthority>(),
                    )
                }
        }.subscribe()

        return ServerResponse.ok().bodyValue(
            mapOf(
                "message" to "Login successful",
                "user" to user.uid
            )
        ).awaitSingle()
    }
}
