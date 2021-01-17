package com.destructo.sushi.ui.other.forums.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.adapter.ForumTopicAdapter
import com.destructo.sushi.databinding.FragmentTopicsBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.network.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TopicsFragment : Fragment() {

    private lateinit var binding: FragmentTopicsBinding
    private lateinit var topicsRecyclerView: RecyclerView
    private lateinit var topicsProgressBar: ProgressBar
    private lateinit var topicsCategoryAdapter: ForumTopicAdapter
    private lateinit var toolbar: Toolbar

    private val topicsViewModel: TopicsViewModel by viewModels()
    private val args: TopicsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            topicsViewModel.getTopicList(args.boardId, null, 40,
                0, "recent", null, null, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTopicsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        topicsRecyclerView = binding.topicRecyclerMain
        topicsRecyclerView.layoutManager = LinearLayoutManager(context)
        topicsProgressBar = binding.topicProgressbar
        toolbar = binding.toolbar


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        topicsCategoryAdapter = ForumTopicAdapter(MalIdListener {

        })

        topicsViewModel.forumTopics.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    topicsProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    resource.data?.let {
                        topicsProgressBar.visibility = View.GONE
                        topicsCategoryAdapter.submitList(it.topicData)
                        topicsRecyclerView.adapter = topicsCategoryAdapter
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: ${resource.message}")
                }
            }

        }

    }
}