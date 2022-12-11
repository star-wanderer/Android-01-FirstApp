package ru.netology.nmedia.dto

data class Post (
     val id: Long,
     val author: String,
     val content: String,
     val published: String,
     var likedByMe: Boolean = false,
     var likeCount: Long = 0,
     var repostCount: Long = 0,
     var visitCount: Long = 0
)