package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun shareById(id: Long)
    fun getAllAsync(callback: GetAllCallback<List<Post>>)
    fun likeAsync(post: Post, callback: GetAllCallback<Post>)
    fun saveAsync(post: Post, callback: GetAllCallback<Post>)
    fun removeByIdAsync(postId: Long, callback: GetAllCallback<Long>)

    interface GetAllCallback<T>{
        fun onSuccess(data: T){}
        fun onError(e: Exception, errorCode: Int){}
    }
}
