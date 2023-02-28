package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun like(post: Post): Post
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}
