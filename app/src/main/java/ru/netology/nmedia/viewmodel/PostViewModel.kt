package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.*

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likeCount = 0,
    shareCount = 0,
    visitCount = 0,
    published = ""
)

class PostViewModel(application : Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao()
    )
    val data = repository.get()
    private val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun save(){
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post){
        edited.value = post
    }

    fun changeContent(content: String){
        edited.value?.let {
            var text = content.trim()
            var link : String? = null
            if (text.substringBefore("\n").contains("https://www.youtube.com")) {
                link = text.substringBefore("\n")
                text = text.substringAfter("\n")
            }
            edited.value = it.copy(content = text, videoLink = link)
        }
    }
}
