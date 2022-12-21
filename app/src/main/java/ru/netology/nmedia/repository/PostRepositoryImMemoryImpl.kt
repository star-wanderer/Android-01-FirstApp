package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryImMemoryImpl: PostRepository{
    private var posts = listOf(
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published ="21 мая в 18:36",
            likedByMe = false,
            likeCount = 999_999,
            shareCount = 600,
            visitCount = 0
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published ="21 мая в 18:36",
            likedByMe = false,
            likeCount = 999_999,
            shareCount = 500,
            visitCount = 0
        ),
        Post(
            id = 3,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published ="21 мая в 18:36",
            likedByMe = false,
            likeCount = 999_999,
            shareCount = 400,
            visitCount = 0
        )
    )

    private val data = MutableLiveData(posts)

    override fun get() = data
    override fun likeById(id : Long){
        posts = posts.map{
            if (it.id != id) it
            else if (it.likedByMe) it.copy(likedByMe = false, likeCount = it.likeCount-1)
                 else it.copy(likedByMe = true, likeCount = it.likeCount+1)
        }
        data.value = posts
    }

    override fun shareById(id: Long){
        posts = posts.map{
            if (it.id != id) it else it.copy(shareCount = it.shareCount+1)
        }
        data.value = posts
    }
}