package ru.netology.nmedia.dto

data class Post (
     val id: Long,
     val author: String,
     val content: String,
     val published: String,
     val likedByMe: Boolean,
     val likeCount: Long = 0,
     val shareCount: Long = 0,
     val visitCount: Long = 0,
     val videoLink: String? = null
)