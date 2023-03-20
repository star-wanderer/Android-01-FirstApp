package ru.netology.nmedia.repository

import retrofit2.Call
import ru.netology.nmedia.api.ApiPosts
import ru.netology.nmedia.dto.Post
import retrofit2.Callback
import retrofit2.Response

class PostRepositoryImpl: PostRepository {
    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback<List<Post>>){
        ApiPosts.retrofitService.getAll().enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(!response.isSuccessful){
                    callback.onError(Exception(response.message()),response.code())
                } else {
                    callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                }
            }
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t), 0x0)
            }
        })
    }

    override fun likeAsync(post: Post, callback: PostRepository.GetAllCallback<Post>) {
        if (!post.likedByMe){
            ApiPosts.retrofitService.like(post.id).enqueue(object: Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        callback.onError(Exception(response.message()),response.code())
                    } else {
                        callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                    }
                }
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t), 0x0)
                }
            })
        } else {
            ApiPosts.retrofitService.dislike(post.id).enqueue(object: Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        callback.onError(Exception(response.message()),response.code())
                    } else {
                        callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                    }
                }
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t), 0x0)
                }
            })
        }
    }

    override fun saveAsync(post: Post, callback: PostRepository.GetAllCallback<Post>) {
        ApiPosts.retrofitService.save(post).enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(!response.isSuccessful){
                        callback.onError(Exception(response.message()),response.code())
                    } else {
                        callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception(t), 0x0)
            }
        })
    }

    override fun removeByIdAsync(postId: Long, callback: PostRepository.GetAllCallback<Long>) {
        ApiPosts.retrofitService.removeById(postId).enqueue(object: Callback<Long>{
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if(!response.isSuccessful){
                    callback.onError(Exception(response.message()),response.code())
                } else {
                    callback.onSuccess(requireNotNull(response.body()) { "body is null" })
                }
            }
            override fun onFailure(call: Call<Long>, t: Throwable) {
                callback.onError(Exception(t), 0x0)
            }
        })
    }
}

