package com.destructo.sushi.ui.other.forums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.ForumCategoryAdapter
import com.destructo.sushi.databinding.FragmentForumBinding
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.getColorFromAttr
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumFragment : Fragment() {

    private lateinit var binding: FragmentForumBinding
    private lateinit var forumBoardRecyclerView: RecyclerView
    private lateinit var forumProgressBar: ProgressBar
    private lateinit var forumCategoryAdapter: ForumCategoryAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: SearchView
    private lateinit var searchEditText: EditText
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
    ): View {
        binding = FragmentForumBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        searchView = binding.forumSearchView
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        context?.let{
            searchEditText.setTextColor(it.getColorFromAttr(R.attr.textColorPrimary))
            searchEditText.setHintTextColor(it.getColorFromAttr(R.attr.textColorSecondary))
            searchEditText.typeface = ResourcesCompat.getFont(it, R.font.poppins_regular)
            searchEditText.textSize = 16f
        }

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        if (it.length >= 3){
//                            searchViewModel.clearMangaList()
//                            searchViewModel.clearAnimeList()
//                            searchViewModel.setQueryString(it)
                        }else{
                            Toast.makeText(context, "Query must be atleast 3 letters", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

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
                    resource.data?.let { it ->
                        forumProgressBar.visibility = View.GONE
                        it.categories?.let {
                            forumCategoryAdapter = ForumCategoryAdapter(it)
                            forumBoardRecyclerView.adapter = forumCategoryAdapter
                        }

                    }
                }
                Status.ERROR -> {
                }
            }

        }

    }

    private fun setupToolbar() {
//        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
//        toolbar.setNavigationOnClickListener {
//            activity?.drawer_layout?.openDrawer(GravityCompat.START)
//        }
    }
}