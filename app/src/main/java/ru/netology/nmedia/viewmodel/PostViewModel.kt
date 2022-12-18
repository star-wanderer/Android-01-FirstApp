package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.*

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryImMemoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun repost() = repository.repost()
}