package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Long = 0,
    val shareCount: Long = 0,
    val visitCount: Long = 0,
    val videoLink: String? = null
) {
    fun toDto() = Post(id, author, content, published, likedByMe, likes, shareCount,visitCount,videoLink )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likes, dto.shareCount,dto.visitCount,dto.videoLink)

    }
}