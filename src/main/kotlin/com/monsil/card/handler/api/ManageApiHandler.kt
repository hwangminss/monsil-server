package com.monsil.card.handler.api

import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.LoginDTO
import com.monsil.card.handler.dto.PhotoDTO
import com.monsil.card.handler.dto.SignUpDTO
import com.monsil.card.service.ManageService
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.core.ParameterizedTypeReference
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Component
class ManageApiHandler (
    private val manageService: ManageService
) {
    companion object : MonSilLog {
        val BAD = ServerResponse.badRequest().build()
        private const val MAIN_UPLOAD_DIR = "/Users/hwangmin/eungming/uploads/main"
        private const val GALLERY_UPLOAD_DIR = "/Users/hwangmin/eungming/uploads/gallery"
        private const val MAX_GALLERY_IMAGES = 20
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
                "user" to user.uid
            )
        ).awaitSingle()
    }

    suspend fun uploadMainPhoto(request: ServerRequest): ServerResponse {
        println("uploadMainPhoto 호출됨")
        return try {
            val data = request.bodyToMono(object : ParameterizedTypeReference<Map<String, String>>() {}).awaitSingleOrNull()
            val photoName = data?.get("name") ?: "background"
            val base64File = data?.get("file")

            if (base64File != null) {
                val bytes = Base64.getDecoder().decode(base64File)
                val filename = "$photoName.jpg"
                val path = Paths.get(MAIN_UPLOAD_DIR).resolve(filename)
                println("파일 경로: $path")

                if (!Files.exists(path.parent)) {
                    println("경로에 대한 디렉토리 생성 중: ${path.parent}")
                    Files.createDirectories(path.parent)
                }

                println("파일을 경로로 전송 중: $path")
                Files.write(path, bytes)
                println("파일 전송 완료")

                val fileUrl = "/Users/hwangmin/eungming/uploads/main/$filename"
                println("파일 URL: $fileUrl")
                val photoDTO = PhotoDTO(url = fileUrl, isMain = true)

                println("서비스에 사진 추가 중: $photoDTO")
                manageService.addMainPt(photoDTO).awaitSingleOrNull()
                println("사진 추가 완료")

                ServerResponse.ok().bodyValueAndAwait(mapOf("url" to fileUrl))
            } else {
                println("파일 데이터가 없음")
                ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .bodyValueAndAwait(mapOf("error" to "파일 데이터가 제공되지 않았습니다"))
            }
        } catch (e: Exception) {
            e.printStackTrace() // 예외 메시지를 로그에 출력
            println("예외 발생: ${e.message}") // 예외 메시지 추가 로그
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValueAndAwait(mapOf("error" to "사진 업로드 실패: ${e.message ?: "알 수 없는 오류"}"))
        }
    }

    // 갤러리 사진 업로드 API (최대 20장)
    suspend fun uploadGalleryPhotos(request: ServerRequest): ServerResponse {
        return try {
            val multipartData = request.multipartData().awaitSingle()
            val files = multipartData["file"]

            if (files != null && files.size <= MAX_GALLERY_IMAGES) {
                val uploadedFiles = mutableListOf<String>()

                for (file in files) {
                    val filePart = file as FilePart
                    val filename = filePart.filename()
                    val path = Paths.get(GALLERY_UPLOAD_DIR).resolve(filename)

                    if (!Files.exists(path.parent)) {
                        Files.createDirectories(path.parent)
                    }

                    filePart.transferTo(path).awaitSingle()
                    uploadedFiles.add("/uploads/gallery/$filename")
                }

                ServerResponse.ok().bodyValueAndAwait(
                    mapOf("message" to "Gallery photos uploaded successfully", "files" to uploadedFiles)
                )
            } else {
                ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .bodyValueAndAwait("No files provided or file limit exceeded. Max 20 images allowed.")
            }
        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValueAndAwait("Gallery photo upload failed: ${e.message}")
        }
    }
}
