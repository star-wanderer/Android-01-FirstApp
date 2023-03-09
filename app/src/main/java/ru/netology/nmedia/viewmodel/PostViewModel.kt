package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    shareCount = 0,
    visitCount = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object: PostRepository.GetAllCallback<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun like(post: Post) {
        repository.likeAsync(post, object: PostRepository.GetAllCallback<Post> {
            override fun onSuccess(data: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map { if (it.id == data.id) data else it }))
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        _data.value = FeedModel(saving = true)
        edited.value?.let { post ->
            repository.saveAsync(post, object: PostRepository.GetAllCallback<Post> {
                override fun onSuccess(data: Post) {
                    _postCreated.postValue(Unit)
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.value = _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        try {
            repository.removeByIdAsync(id, object: PostRepository.GetAllCallback<Long> {
                override fun onSuccess(data: Long) {
                    _data.postValue(FeedModel(deleted = true))
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        } catch (e: IOException) {
            _data.value = _data.value?.copy(posts = old)
        }
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

    fun shareById(id: Long) {
        thread { repository.shareById(id) }
    }
}
