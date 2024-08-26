package com.monsil.card.handler.view


import com.monsil.card.config.MonSilLog
import com.monsil.card.handler.SessionHandler
import com.monsil.card.handler.SessionHandler.Companion.HTML
import com.monsil.card.repository.family.FamilyEntity
import com.monsil.card.repository.family.FamilyRepository
import com.monsil.card.repository.guestbook.GuestBookRepository
import com.monsil.card.repository.manager.ManagerRepository
import com.monsil.card.repository.photo.PhotoRepository
import com.monsil.card.service.FamilyService
import com.monsil.card.service.GuestBookService
import com.monsil.card.service.ManageService
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class ViewHandler(
    private val familyService: FamilyService,
    private val familyRepository: FamilyRepository,
    private val managerService: ManageService,
    private val managerRepository: ManagerRepository,
    private val guestbookServicde: GuestBookService,
    private val guestBookRepository: GuestBookRepository,
    private val photoRepository: PhotoRepository
) : SessionHandler {
    companion object : MonSilLog

    suspend fun index(request: ServerRequest): ServerResponse {
        val bgImg = photoRepository.findByIdAndIsMain(1,1).awaitSingleOrNull()
        val gallery = photoRepository.findByIsMain(0).collectList().awaitSingleOrNull()
        return HTML.render(
            "index/index",
            mapOf(
                "bgImg" to bgImg,
                "gallery" to gallery
            )
        ).awaitSingle()
    }

    suspend fun main(request: ServerRequest): ServerResponse {
        return HTML.render("main/main").awaitSingle()
    }

    suspend fun login(request: ServerRequest): ServerResponse {
        return HTML.render("login/login").awaitSingle()
    }

    suspend fun home(request: ServerRequest): ServerResponse {
        val uid = request.userSession()["userUid"] as String
        val user = managerRepository.findByUid(uid).awaitSingle()
        return HTML.render(
            "manager/home",
            mapOf("user" to user)
        ).awaitSingle()
    }

    suspend fun family(request: ServerRequest): ServerResponse {
        val uid = request.userSession()["userUid"] as String
        val user = managerRepository.findByUid(uid).awaitSingle()
        val roleData = getRoleData(user.role!!)

        return HTML.render(
            "manager/family",
            mapOf("userList" to roleData)
        ).awaitSingle()
    }

    suspend fun guestbook(request: ServerRequest): ServerResponse {
        return HTML.render(
            "manager/guestbook",
            mapOf(
                "guestbook" to guestBookRepository.findAll().collectList().awaitSingleOrNull()
            )
        ).awaitSingle()
    }

    suspend fun mainPhoto(request: ServerRequest): ServerResponse {
        val mainPt = photoRepository.findByIdAndIsMain(1,1).awaitSingleOrNull()
        return HTML.render(
            "manager/mainPhoto",
            mapOf("data" to mainPt)
        ).awaitSingle()
    }

    suspend fun galleryPhoto(request: ServerRequest): ServerResponse {
        val gallery = photoRepository.findByIsMain(0).collectList().awaitSingleOrNull()
        return HTML.render(
            "manager/galleryPhoto",
            mapOf("images" to gallery)
            ).awaitSingle()
    }

    private suspend fun getRoleData(role: Int): List<FamilyEntity> {
        return when (role) {
            0 -> familyRepository.findAllByDeletedAtIsNullOrderByRole().collectList().awaitSingle()
            1 -> familyRepository.findByGroombride(0).collectList().awaitSingle()
            else -> familyRepository.findByGroombride(1).collectList().awaitSingle()
        }
    }
}