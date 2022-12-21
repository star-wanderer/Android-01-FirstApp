package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.CountView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostsAdapter (
    private val OnLikeListener: OnLikeListener,
    private val OnShareListener: OnShareListener
) : ListAdapter<Post,PostViewHolder>(PostDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, OnLikeListener,OnShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val OnLikeListener: OnLikeListener,
    private val OnShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
        fun bind (post: Post){
            binding.apply {
                likeCount.text = CountView.convert(post.likeCount)
                shareCount.text = CountView.convert(post.shareCount)
                visitCount.text = CountView.convert(post.visitCount)
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
                like.setOnClickListener {
                    OnLikeListener(post)
                }
                share.setOnClickListener {
                    OnShareListener(post)
                }
            }
        }
    }

class PostDiffCallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
