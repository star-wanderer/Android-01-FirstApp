package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryImMemoryImpl: PostRepository{
    private var post = Post (
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published ="21 мая в 18:36",
        likedByMe = false,
        likeCount = 999_999,
        repostCount = 999,
        visitCount = 0
    )

    private val data = MutableLiveData(post)

    override fun get() = data
    override fun like(){
        post = post.copy(likedByMe = !post.likedByMe,likeCount = if (post.likedByMe) post.likeCount-1 else post.likeCount+1)
        data.value = post
    }

    override fun repost(){
        post = post.copy(repostCount = post.repostCount+1)
        data.value = post
    }
}