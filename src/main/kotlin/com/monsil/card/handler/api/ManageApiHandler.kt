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
//        private const val MAIN_UPLOAD_DIR = "/Users/hwangmin/eungming/uploads/main"
//        private const val GALLERY_UPLOAD_DIR = "/Users/hwangmin/eungming/uploads/gallery"
        private const val MAIN_UPLOAD_DIR = "/uploads/main"
        private const val GALLERY_UPLOAD_DIR = "/uploads/gallery"
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
        println("upload")
        return try {
            val data = request.bodyToMono(object : ParameterizedTypeReference<Map<String, String>>() {}).awaitSingleOrNull()
            val base64File = data?.get("file")

            if (base64File != null) {
                val bytes = Base64.getDecoder().decode(base64File)
                val mimeType = Files.probeContentType(Paths.get("dummy", "dummy").resolve("dummy.jpg"))
                val extension = when (mimeType) {
                    "image/jpeg" -> "jpg"
                    "image/png" -> "png"
                    "image/gif" -> "gif"
                    else -> "jpg"
                }
                val filename = "background.$extension"
                val path = Paths.get(MAIN_UPLOAD_DIR).resolve(filename)

                if (!Files.exists(path.parent)) {
                    Files.createDirectories(path.parent)
                }

                Files.write(path, bytes)
                val fileUrl = "/uploads/main/$filename"
                val photoDTO = PhotoDTO(url = fileUrl, isMain = true)
                manageService.addMainPt(photoDTO).awaitSingleOrNull()

                ServerResponse.ok().bodyValueAndAwait(mapOf("url" to fileUrl))
            } else {
                ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .bodyValueAndAwait(mapOf("error" to "파일 데이터가 제공되지 않았습니다"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValueAndAwait(mapOf("error" to "사진 업로드 실패: ${e.message ?: "알 수 없는 오류"}"))
        }
    }

    suspend fun modifyMainPhoto(request: ServerRequest): ServerResponse {
        println("modify")
        return try {
            val data = request.bodyToMono(object : ParameterizedTypeReference<Map<String, String>>() {}).awaitSingleOrNull()
            val base64File = data?.get("file")
            val exFile = manageService.loadMainPt()
            val exFileName = exFile.url
            println("$exFile \n $exFileName")
            if (exFile != null) {
                val path = Paths.get(MAIN_UPLOAD_DIR).resolve(exFileName)

                if (Files.exists(path)) {
                    Files.delete(path)
                    ServerResponse.ok().bodyValueAndAwait(mapOf("message" to "파일이 성공적으로 삭제되었습니다"))
                } else {
                    ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValueAndAwait(mapOf("error" to "파일을 찾을 수 없습니다"))
                }
            } else {
                ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .bodyValueAndAwait(mapOf("error" to "파일 이름이 제공되지 않았습니다"))
            }

            if (base64File != null) {
                val bytes = Base64.getDecoder().decode(base64File)
                val mimeType = Files.probeContentType(Paths.get("dummy", "dummy").resolve("dummy.jpg"))
                val extension = when (mimeType) {
                    "image/jpeg" -> "jpg"
                    "image/png" -> "png"
                    "image/gif" -> "gif"
                    else -> "jpg"
                }
                val filename = "background.$extension"
                val path = Paths.get(MAIN_UPLOAD_DIR).resolve(filename)

                if (!Files.exists(path.parent)) {
                    Files.createDirectories(path.parent)
                }

                Files.write(path, bytes)
                val fileUrl = "/uploads/main/$filename"
                val photoDTO = PhotoDTO(url = fileUrl, isMain = true)
                manageService.modifyMainPt(photoDTO)

                ServerResponse.ok().bodyValueAndAwait(mapOf("url" to fileUrl))
            } else {
                ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .bodyValueAndAwait(mapOf("error" to "파일 데이터가 제공되지 않았습니다"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
