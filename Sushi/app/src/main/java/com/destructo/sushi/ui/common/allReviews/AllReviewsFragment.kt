package com.destructo.sushi.ui.common.allReviews

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
import com.destructo.sushi.adapter.AllAnimeReviewsAdapter
import com.destructo.sushi.databinding.FragmentAllReviewsBinding
import com.destructo.sushi.listener.AnimeReviewListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.animeDetails.AnimeReviewBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AllReviewsFragment : Fragment() {

    private lateinit var binding: FragmentAllReviewsBinding
    private val allReviewsViewModel: AllReviewViewModel by viewModels()
    private var animeIdArg: Int = 0
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewsAdapter: AllAnimeReviewsAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null){
            animeIdArg = savedInstanceState.getInt("animeId")
        }else{
            animeIdArg = AllReviewsFragmentArgs.fromBundle(requireArguments()).animeId
            allReviewsViewModel.getAnimeReviews(animeIdArg)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("animeId", animeIdArg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllReviewsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        reviewsRecyclerView = binding.reviewRecycler
        reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
        toolbar = binding.toolbar

        setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        reviewsAdapter = AllAnimeReviewsAdapter(AnimeReviewListener {
            it?.let {
                val reviewDialog = AnimeReviewBottomSheetFragment.newInstance(it)
                reviewDialog.show(childFragmentManager, "anime_review_dialog")
            }

        })
        reviewsRecyclerView.adapter = reviewsAdapter

        allReviewsViewModel.animeReview.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.let { animeReviews ->
                        reviewsAdapter.submitList(animeReviews.reviews)
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