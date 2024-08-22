package com.monsil.card.service

import com.moimpay.web.exception.CustomException
import com.moimpay.web.exception.ErrorCode
import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.LoginDTO
import com.monsil.card.handler.dto.PhotoDTO
import com.monsil.card.handler.dto.SignUpDTO
import com.monsil.card.repository.manager.ManagerEntity
import com.monsil.card.repository.manager.ManagerRepository
import com.monsil.card.repository.photo.PhotoEntity
import com.monsil.card.repository.photo.PhotoRepository
import com.monsil.card.util.PasswordEncoderImpl
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class ManageService(
    private val managerRepository: ManagerRepository,
    private val photoRepository: PhotoRepository
) {
    companion object : MonSilLog

    suspend fun signUp(signUpDTO: SignUpDTO): Mono<ManagerEntity> {
        val signup = ManagerEntity.assign(signUpDTO)
        return managerRepository.save(signup)
    }

    suspend fun login(login: LoginDTO): ManagerEntity {
        val user = managerRepository.findByNameAndDeletedAtNull(login.name)
            .awaitSingleOrNull() ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        if (user.password.isNotEmpty()) {
            val isMatch = PasswordEncoderImpl.matches(login.password, user.password)
            if (!isMatch) {
                throw CustomException(ErrorCode.NOT_EQUAL_PASSWORD)
            }
        }

        return user
    }

    suspend fun addMainPt(photo: PhotoDTO): Mono<PhotoEntity> {
        val photoEntity = PhotoEntity.assign(photo)
        return photoRepository.save(photoEntity)
    }

    suspend fun loadMainPt(): PhotoEntity {
        val exFile = photoRepository.findByIsMain(1).awaitSingleOrNull()
            ?: throw CustomException(ErrorCode.NOT_EXIST_USER)
        return exFile
    }

    suspend fun modifyMainPt(photo: PhotoDTO): PhotoEntity {
        return photoRepository.findByIsMain(1).flatMap {
            it.url = photo.url
            it.updatedAt = LocalDateTime.now()
            photoRepository.save(it)
        }.awaitSingle()
    }
}
