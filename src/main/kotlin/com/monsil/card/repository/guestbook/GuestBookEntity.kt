package com.monsil.card.repository.guestbook

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("GuestBook")
data class GuestBookEntity(
    @Id
    @Column("id")
    var id: Long? = null
) {
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
