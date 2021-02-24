package com.destructo.sushi.ui.anime.animeReviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.adapter.AllAnimeReviewsAdapter
import com.destructo.sushi.databinding.FragmentAllAnimeReviewsBinding
import com.destructo.sushi.listener.AnimeReviewListener
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.network.Status
import com.destructo.sushi.room.AnimeReviewListDao
import com.destructo.sushi.ui.anime.animeDetails.AnimeReviewBottomSheetFragment
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.ListItemVerticalDecor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AnimeReviewsFragment : BaseFragment() {

    private lateinit var binding: FragmentAllAnimeReviewsBinding
    private val allReviewsViewModel: AnimeReviewsViewModel by viewModels()
    private val args: AnimeReviewsFragmentArgs by navArgs()
    private var animeIdArg: Int = 0
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewsAdapter: AllAnimeReviewsAdapter
    private lateinit var toolbar: Toolbar

    @Inject
    lateinit var animeReviewsDao: AnimeReviewListDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null){
            animeIdArg = args.animeId
        }else{
            animeIdArg = args.animeId
            allReviewsViewModel.getAnimeReviews(animeIdArg, "1")

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("animeId", animeIdArg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllAnimeReviewsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        reviewsRecyclerView = binding.reviewRecycler
        reviewsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        reviewsRecyclerView.addItemDecoration(ListItemVerticalDecor(LIST_SPACE_HEIGHT))
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
        reviewsAdapter.setListEndListener(object : ListEndListener{
            override fun onEndReached(position: Int) {
                allReviewsViewModel.loadNextPage(animeIdArg)
            }
        })

        reviewsRecyclerView.adapter = reviewsAdapter

        allReviewsViewModel.animeReview.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    resource.data?.let {
                        reviewsAdapter
                            .submitList(allReviewsViewModel.getReviewListById(animeIdArg)?.reviews)
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