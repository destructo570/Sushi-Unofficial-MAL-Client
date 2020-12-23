package com.destructo.sushi.ui.manga.allMangaReviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.adapter.AllMangaReviewAdapter
import com.destructo.sushi.databinding.FragmentAllMangaReviewsBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MangaReviewListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.manga.mangaDetails.MangaReviewBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AllMangaReviews : Fragment() {

    private lateinit var binding: FragmentAllMangaReviewsBinding
    private val allReviewsViewModel: AllMangaReviewViewModel by viewModels()
    private var mangaIdArg: Int = 0
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewsAdapter: AllMangaReviewAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            mangaIdArg = savedInstanceState.getInt("mangaId")
        }else{
            mangaIdArg = AllMangaReviewsArgs.fromBundle(requireArguments()).mangaId
            allReviewsViewModel.getMangaReviews(mangaIdArg, "1")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("mangaId", mangaIdArg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllMangaReviewsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        reviewsRecyclerView = binding.reviewRecycler
        reviewsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        toolbar = binding.toolbar

        setupToolbar()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        reviewsAdapter = AllMangaReviewAdapter(MangaReviewListener {
            it?.let {
                val reviewDialog = MangaReviewBottomSheetFragment.newInstance(it)
                reviewDialog.show(childFragmentManager, "manga_review_dialog")
            }
        })
        reviewsAdapter.setListEndListener(object : ListEndListener {
            override fun onEndReached(position: Int) {
                allReviewsViewModel.loadNextPage(mangaIdArg)
            }
        })
        reviewsRecyclerView.adapter = reviewsAdapter

        allReviewsViewModel.mangaReview.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    resource.data?.let { mangaReviews ->
                        reviewsAdapter
                            .submitList(allReviewsViewModel.getReviewListById(mangaIdArg)?.reviews)
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }

            }
        }
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }

}