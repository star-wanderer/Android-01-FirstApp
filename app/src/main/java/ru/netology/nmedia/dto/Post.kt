package ru.netology.nmedia.dto

data class Post (
     val id: Long = 0L,
     val author: String = "",
     val content: String = "",
     val published: String= "",
     val likedByMe: Boolean = false,
     val likeCount: Long = 0,
     val shareCount: Long = 0,
     val visitCount: Long = 0,
     val videoLink: String? = null
)