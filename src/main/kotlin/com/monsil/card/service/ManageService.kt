package com.monsil.card.service

import com.moimpay.web.exception.CustomException
import com.moimpay.web.exception.ErrorCode
import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.dto.LoginDTO
import com.monsil.card.handler.dto.SignUpDTO
import com.monsil.card.repository.manager.ManagerEntity
import com.monsil.card.repository.manager.ManagerRepository
import com.monsil.card.util.PasswordEncoderImpl
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ManageService(
    private val managerRepository: ManagerRepository
) {
    companion object : MonSilLog

    suspend fun signUp(signUpDTO: SignUpDTO): Mono<ManagerEntity> {
        val signup = ManagerEntity.assign(signUpDTO)
        return managerRepository.save(signup)
    }

    suspend fun login(login: LoginDTO): ManagerEntity {
        val user = managerRepository.findUserByNameAndDeletedAtNull(login.name)
            .awaitSingleOrNull() ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        if (user.password.isNotEmpty()) {
            val isMatch = PasswordEncoderImpl.matches(login.password, user.password)
            if (!isMatch) {
                throw CustomException(ErrorCode.NOT_EQUAL_PASSWORD)
            }
        }

        return user
    }
}
