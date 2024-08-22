package com.monsil.card.repository.photo

import com.monsil.card.handler.dto.PhotoDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("Photo")
data class PhotoEntity(
    @Id
    @Column("id")
    var id: Long? = null
) {
    companion object {
        fun assign(pt: PhotoDTO): PhotoEntity {
            return PhotoEntity(id = null).apply {
                url = pt.url
                isMain = pt.isMain
            }
        }
    }

    @Column("url")
    var url: String? = null

    @Column("is_main")
    var isMain: Boolean? = null

    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
}
