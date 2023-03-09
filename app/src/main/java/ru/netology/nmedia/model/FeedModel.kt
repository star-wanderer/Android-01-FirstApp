package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
    val deleted: Boolean = false,
    val saved: Boolean = false,
    val saving: Boolean = false,
    val deleting: Boolean = false,
    val refreshing: Boolean = false,
)