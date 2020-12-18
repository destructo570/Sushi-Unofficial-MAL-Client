package com.destructo.sushi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentPagerAdapter
import com.destructo.sushi.databinding.FragmentSearchBinding
import com.destructo.sushi.util.getColorFromAttr
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar
    private lateinit var resultPager: ViewPager2
    private lateinit var resultPagerAdapter: FragmentStateAdapter
    private lateinit var resultTabLayout: TabLayout
    private lateinit var resultTabMediator: TabLayoutMediator
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }

        searchView = binding.searchView
        searchView.setIconifiedByDefault(false)
        searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        context?.let{
            searchEditText.setTextColor(it.getColorFromAttr(R.attr.textColorPrimary))
            searchEditText.setHintTextColor(it.getColorFromAttr(R.attr.textColorSecondary))
            searchEditText.typeface = ResourcesCompat.getFont(it, R.font.poppins_regular)
            searchEditText.textSize = 16f
        }
        searchView.requestFocus()

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (!it.isBlank()) {
                        if (it.length >= 3){
                            searchViewModel.clearMangaList()
                            searchViewModel.clearAnimeList()
                            searchViewModel.setQueryString(it)
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

        resultPager = binding.resultViewPager
        resultPager.offscreenPageLimit = 1
        resultTabLayout = binding.resultTabLayout

        resultTabMediator = TabLayoutMediator(resultTabLayout, resultPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.search_anime)
                }
                1 -> {
                    tab.text = getString(R.string.search_manga)
                }
            }
        }

        toolbar = binding.toolbar

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
            val fragmentList = arrayListOf(
                AnimeResultFragment(),
                MangaResultFragment(),
            )
            setupViewPager(fragmentList)
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_fill)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
    }

    private fun setupViewPager(fragmentList:ArrayList<Fragment>){
        resultPagerAdapter = FragmentPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle)
        resultPager.adapter = resultPagerAdapter
        resultTabMediator.attach()
    }
}