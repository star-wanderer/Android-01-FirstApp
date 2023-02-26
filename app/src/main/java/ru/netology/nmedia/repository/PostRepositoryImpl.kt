package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val typeToken2 = object : TypeToken<Post>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    private fun getById(id: Long) : Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("post with id: $id does not exist") }
            .let {
                gson.fromJson(it, typeToken2.type)
            }
    }

    override fun likeById(id: Long) {

        val post = getById(id)
        val request = if (post.likedByMe){
                Request.Builder()
                    .delete()
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .build()
            } else {
                Request.Builder()
                    .post(gson.toJson(post).toRequestBody(jsonType))
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .build()
            }
            client.newCall(request)
                .execute()
                .close()
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }
}