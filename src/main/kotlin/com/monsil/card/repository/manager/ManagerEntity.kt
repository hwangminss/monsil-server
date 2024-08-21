package com.monsil.card.repository.manager

import com.monsil.card.handler.dto.SignUpDTO
import com.monsil.card.util.PasswordEncoderImpl
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("Manager")
data class ManagerEntity(
    @Column("uid")
    var uid: String = UUID.randomUUID().toString().split("-").joinToString(""),
    @Column("name")
    var name: String,
) {
    companion object {
        fun assign(signUpDTO: SignUpDTO): ManagerEntity {
            val map = PasswordEncoderImpl.initEncode(signUpDTO.password)
            return ManagerEntity(name = signUpDTO.name).apply {
                password = map["hashpw"] ?: ""
                salt = map["salt"]
                role = signUpDTO.role
            }
        }
    }

    @Id
    @Column("id")
    var id: Long? = null

    @Column("passwd")
    lateinit var password: String

    @Column("salt")
    var salt: String? = null

    @Column("role")
    var role: Int? = null

    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @Column("deleted_at")
    var deletedAt: LocalDateTime? = null
}

