package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST1
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST2
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST3
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST4
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST5
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST6
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST7
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST8
import ru.netology.nmedia.activity.ViewPostFragment.Companion.ARG_POST9
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.*

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostsAdapter(object: OnInteractionListener {

            override fun onPostContent(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_viewPostFragment,
                    Bundle().apply {
                        ARG_POST1 = post.id.toString()
                        ARG_POST2 = post.likedByMe.toString()
                        ARG_POST3 = post.likeCount.toString()
                        ARG_POST4 = post.shareCount.toString()
                        ARG_POST5 = post.content
                        ARG_POST6 = post.visitCount.toString()
                        ARG_POST7 = post.author
                        ARG_POST8 = post.published
                        ARG_POST9 = post.videoLink
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
                viewModel.likeById(post.id)
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
        viewModel.data.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
        }
        return binding.root
    }
}
