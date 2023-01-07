package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositoryFileImpl (
    private val context: Context
): PostRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 1L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                posts = gson.fromJson(it, type)
                data.value = posts
            }
        } else {
            sync()
        }
    }

    private fun sync(){
        context.openFileOutput(filename,Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
    override fun get() = data

    override fun likeById(id : Long){
        posts = posts.map{
            if (it.id != id) it
            else if (it.likedByMe) it.copy(likedByMe = false, likeCount = it.likeCount-1)
            else it.copy(likedByMe = true, likeCount = it.likeCount+1)
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long){
        posts = posts.map{
            if (it.id != id) it else it.copy(shareCount = it.shareCount+1)
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long){
        posts = posts.filter{ it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L){
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now")
            ) + posts
            data.value = posts
            sync()
            return
        }
        posts = posts.map{
            if (it.id != post.id) it else it.copy(content = post.content, videoLink = post.videoLink)
        }
        data.value = posts
        sync()
    }
}