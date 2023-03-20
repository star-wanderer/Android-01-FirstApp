package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST_ID
import ru.netology.nmedia.operation.Operation

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        binding.swipeRefresh.setOnRefreshListener{
            viewModel.loadPosts()
            binding.swipeRefresh.isRefreshing = false
        }

        val adapter = PostsAdapter(object: OnInteractionListener {

            override fun onPostContent(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_viewPostFragment,
                    Bundle().apply {
                        ARG_POST_ID = post.id.toString()
                    }
                )
            }

            override fun onVideo (post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(post.videoLink)
                }
                startActivity(intent)
            }

            override fun onVideoControl(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(post.videoLink)
                }
                startActivity(intent)
            }

            override fun onLike (post: Post) {
                viewModel.like(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        textArg = if (post.videoLink.isNullOrBlank()) post.content
                        else post.videoLink + "\n" + post.content
                    })
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,post.content)
                    type = "text/plain"
                }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
            binding.savingPost.isVisible = state.saving

            binding.issueGroup.isVisible = state.issue
            binding.issueTitle.text = state.issueText
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.issueButton.setOnClickListener {
            when (viewModel.lastOperationCode) {
                Operation.LIKE -> viewModel.like(viewModel.lastPost)
                Operation.SAVE -> viewModel.save()
                Operation.EDIT -> viewModel.edit(viewModel.lastPost)
                Operation.DELETE -> viewModel.removeById(viewModel.lastId)
                else -> { }
            }
        }

        binding.issueCancelButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
        }
        return binding.root
    }
}
