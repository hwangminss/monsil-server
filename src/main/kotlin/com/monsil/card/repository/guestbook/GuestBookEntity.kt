package com.monsil.card.repository.guestbook

import com.monsil.card.handler.dto.GuestBookDTO
import com.monsil.card.util.PasswordEncoderImpl
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("GuestBook")
data class GuestBookEntity(
    @Id
    @Column("id")
    var id: Long? = null
) {
    companion object {
        fun assign(guestBookDTO: GuestBookDTO): GuestBookEntity {
            val map = PasswordEncoderImpl.initEncode(guestBookDTO.password)
            return GuestBookEntity(id = null).apply {
                name = guestBookDTO.name
                detail = guestBookDTO.detail
                password = map["hashpw"] ?: ""
                salt = map["salt"]
            }
        }
    }

    @Column("name")
    var name: String? = null

    @Column("detail")
    var detail: String? = null

    @Column("passwd")
    lateinit var password: String

    @Column("salt")
    var salt: String? = null

    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @Column("deleted_at")
    var deletedAt: LocalDateTime? = null
}
