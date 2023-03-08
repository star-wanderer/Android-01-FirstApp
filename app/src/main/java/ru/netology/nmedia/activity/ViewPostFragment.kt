package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentViewPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.presenter.CountView
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class ViewPostFragment : Fragment() {

    companion object {
        var Bundle.ARG_POST_ID: String? by StringArg
    }

    private lateinit var binding : FragmentViewPostBinding

    private lateinit var myPost : Post

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentViewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.let { args ->

            val postId = args.ARG_POST_ID.toString().toLong()

            with(binding){

                video.setOnClickListener{
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(myPost.videoLink)
                    }
                    startActivity(intent)
                }

                videoControl.setOnClickListener{
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(myPost.videoLink)
                    }
                    startActivity(intent)
                }

                like.setOnClickListener {
                    like.isChecked = myPost.likedByMe
                    myPost.id.let { viewModel.like(myPost) }
                }

                share.setOnClickListener {
                    myPost.id.let { id -> viewModel.shareById(id) }
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, myPost.content)
                        type = "text/plain"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }

                menu.setOnClickListener {
                    PopupMenu(it.context,it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    postId.let { id -> viewModel.removeById(id) }
                                    findNavController().navigateUp()
                                }
                                R.id.edit -> {
                                    viewModel.edit(myPost)
                                    findNavController().navigate(
                                        R.id.action_viewPostFragment_to_editPostFragment,
                                        Bundle().apply {
                                            textArg = if (myPost.videoLink.isNullOrBlank()) myPost.content
                                            else myPost.videoLink + "\n" + myPost.content
                                        })
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }

            viewModel.data.observe(viewLifecycleOwner){ updatedPosts ->
                updatedPosts.posts.lastOrNull { it.id == postId }?.let { updateView(it) }
            }
        }
        return binding.root

    }

    private fun updateView(post: Post) {

        myPost = post

        with(binding) {
            like.text = CountView.convert(post.likes)
            share.text = CountView.convert(post.shareCount)
            visit.text = CountView.convert(post.visitCount)
            author.text = post.author
            content.text = post.content
            published.text = post.published
            like.isChecked = post.likedByMe

            if (post.videoLink.isNullOrBlank()) {
                binding.group.visibility = View.GONE
            } else binding.group.visibility = View.VISIBLE
        }
    }
}