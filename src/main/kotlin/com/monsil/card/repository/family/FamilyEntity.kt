package com.monsil.card.repository.family

import com.monsil.card.handler.dto.FamilyDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("FamilyInfo")
data class FamilyEntity(
    @Id
    @Column("id")
    var id: Long? = null
) {
    companion object {
        fun assign(family: FamilyDTO): FamilyEntity {
            return FamilyEntity(id = null).apply {
                role = family.role
                name = family.name
                account = family.account
                kakao = family.kakao
                phone = "tel:${family.phone}"
                message = "sms:${family.phone}"
                groombride = family.groombride

            }
        }
    }

    @Column("role")
    var role: Int? = null

    @Column("name")
    var name: String? = null

    @Column("bank")
    var bank: String? = null

    @Column("account")
    var account: String? = null

    @Column("kakao")
    var kakao: String? = null

    @Column("phone")
    var phone: String? = null

    @Column("message")
    var message: String? = null

    @Column("groombride")
    var groombride: Boolean? = null

    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @Column("deleted_at")
    var deletedAt: LocalDateTime? = null
}
