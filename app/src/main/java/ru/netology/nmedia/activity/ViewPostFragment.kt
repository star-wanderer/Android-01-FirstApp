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
        var Bundle.ARG_POST1: String? by StringArg
        var Bundle.ARG_POST2: String? by StringArg
        var Bundle.ARG_POST3: String? by StringArg
        var Bundle.ARG_POST4: String? by StringArg
        var Bundle.ARG_POST5: String? by StringArg
        var Bundle.ARG_POST6: String? by StringArg
        var Bundle.ARG_POST7: String? by StringArg
        var Bundle.ARG_POST8: String? by StringArg
        var Bundle.ARG_POST9: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentViewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.let { args ->

            val post = Post (
                id = args.ARG_POST1.toString().toLong(),
                likedByMe = args.ARG_POST2.toString().toBoolean(),
                likeCount = args.ARG_POST3.toString().toLong(),
                shareCount = args.ARG_POST4.toString().toLong(),
                content = args.ARG_POST5.toString(),
                visitCount = args.ARG_POST6.toString().toLong(),
                author = args.ARG_POST7.toString(),
                published = args.ARG_POST8.toString(),
                videoLink = args.ARG_POST9.toString()
            )

            with(binding) {
                like.text = CountView.convert(post.likeCount)
                share.text = CountView.convert(post.shareCount)
                visit.text = CountView.convert(post.visitCount)
                author.text = post.author
                content.text = post.content
                published.text = post.published

                if (post.videoLink.isNullOrBlank()) {
                    binding.group.visibility = View.GONE
                } else binding.group.visibility = View.VISIBLE

                video.setOnClickListener{
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(post.videoLink)
                    }
                    startActivity(intent)
                }

                videoControl.setOnClickListener{
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(post.videoLink)
                    }
                    startActivity(intent)
                }

                like.setOnClickListener {
                    like.isChecked = args.ARG_POST2.toBoolean()
                    args.ARG_POST1?.let { it1 -> viewModel.likeById(it1.toLong()) }
                }

                share.setOnClickListener {
                    args.ARG_POST1?.let { id -> viewModel.shareById(id.toLong()) }
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, args.ARG_POST5)
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
                                    args.ARG_POST1?.let { id -> viewModel.removeById(id.toLong()) }
                                    findNavController().navigateUp()
                                }
                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController().navigate(
                                        R.id.action_viewPostFragment_to_editPostFragment,
                                        Bundle().apply {
                                            textArg = if (post.videoLink.isNullOrBlank()) post.content
                                            else post.videoLink + "\n" + post.content
                                        })
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }

//            What to pass to subscribe to single post data updates?
//            viewModel.data.observe(viewLifecycleOwner){
//                ??????
//            }
        }
        return binding.root

    }
}