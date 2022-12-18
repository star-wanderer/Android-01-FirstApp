package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.*

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) {
            with(binding) {
                likeCount.text = CountView.convert(it.likeCount)
                repostCount.text = CountView.convert(it.repostCount)
                visitCount.text = CountView.convert(it.visitCount)
                author.text = it.author
                published.text = it.published
                content.text = it.content
                like.setImageResource(
                    if (it.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
            }
        }

        with(binding) {
            like.setOnClickListener {
                viewModel.like()
            }

            repost.setOnClickListener {
                viewModel.repost()
            }
        }
    }
}