package com.destructo.sushi.ui.other.forums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.adapter.pagerAdapter.ForumCategoryAdapter
import com.destructo.sushi.databinding.FragmentForumBinding
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ForumFragment : Fragment() {

    private lateinit var binding: FragmentForumBinding
    private lateinit var forumBoardRecyclerView: RecyclerView
    private lateinit var forumProgressBar: ProgressBar
    private lateinit var forumCategoryAdapter: ForumCategoryAdapter
    private lateinit var toolbar: Toolbar
    private val forumViewModel: ForumBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            forumViewModel.getForumBoardList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForumBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        forumBoardRecyclerView = binding.forumRecyclerMain
        forumBoardRecyclerView.layoutManager = LinearLayoutManager(context)
        forumProgressBar = binding.forumProgressbar
        toolbar = binding.toolbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        forumViewModel.forumBoards.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    forumProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        forumProgressBar.visibility = View.GONE
                        forumCategoryAdapter = ForumCategoryAdapter(it.categories)
                        forumBoardRecyclerView.adapter = forumCategoryAdapter
                    }
                }
                Status.ERROR -> {
                }
            }

        }

    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }
}