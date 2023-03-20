package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.presenter.CountView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike (post: Post) {}
    fun onShare (post: Post) {}
    fun onRemove (post: Post) {}
    fun onEdit (post: Post) {}
    fun onVideo (post: Post) {}
    fun onVideoControl (post: Post) {}
    fun onPostContent (post: Post) {}
}

class PostsAdapter (
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post,PostViewHolder>(PostDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)

    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
        fun bind (post: Post){
            binding.apply {
                like.isChecked = post.likedByMe
                like.text = CountView.convert(post.likes)
                share.text = CountView.convert(post.shareCount)
                visit.text = CountView.convert(post.visitCount)
                author.text = post.author
                published.text = post.published
                content.text = post.content

                var url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
                Glide.with(binding.avatar)
                    .load(url)
                    .placeholder(R.drawable.ic_baseline_question_mark_48dp)
                    .error(R.drawable.ic_baseline_error_48dp)
                    .timeout(10_000)
                    .circleCrop()
                    .into(binding.avatar)

                url = "http://10.0.2.2:9999/images/${post.attachment?.url}"
                Glide.with(binding.image)
                    .load(url)
                    .timeout(10_000)
                    .centerInside()
                    .into(binding.image)

                if (post.videoLink.isNullOrBlank()) {
                    binding.group.visibility = View.GONE
                } else binding.group.visibility = View.VISIBLE

                content.setOnClickListener {
                    onInteractionListener.onPostContent(post)
                }

                video.setOnClickListener {
                    onInteractionListener.onVideo(post)
                }

                videoControl.setOnClickListener {
                    onInteractionListener.onVideoControl(post)
                }

                like.setOnClickListener {
                    like.isChecked = post.likedByMe
                    onInteractionListener.onLike(post)
                }
                share.setOnClickListener {
                    onInteractionListener.onShare(post)
                }

                menu.setOnClickListener {
                    PopupMenu(it.context,it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
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
