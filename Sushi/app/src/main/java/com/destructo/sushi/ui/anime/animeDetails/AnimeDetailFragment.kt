package com.destructo.sushi.ui.anime.animeDetails

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeDetailBinding
import com.destructo.sushi.ui.anime.adapter.*
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.inc_anime_detail_sub_desc.view.*
import kotlinx.android.synthetic.main.inc_anime_videos.view.*
import kotlinx.android.synthetic.main.inc_characters_list.view.*
import kotlinx.android.synthetic.main.inc_my_anime_status.view.*
import kotlinx.android.synthetic.main.inc_recomms_list.view.*
import kotlinx.android.synthetic.main.inc_related_anime.view.*
import kotlinx.android.synthetic.main.inc_review_list.view.*
import kotlinx.android.synthetic.main.inc_staff_list.view.*
import timber.log.Timber


@AndroidEntryPoint
class AnimeDetailFragment : Fragment() {


    private lateinit var binding: FragmentAnimeDetailBinding
    private var animeIdArg: Int = 0
    private val animeDetailViewModel: AnimeDetailViewModel by viewModels()
    private lateinit var scoreCardView: MaterialCardView
    private lateinit var coverView: ImageView
    private lateinit var scoreTextView: TextView
    private lateinit var toolbar: Toolbar

    private lateinit var characterAdapter: AnimeCharacterListAdapter
    private lateinit var staffAdapter: AnimeStaffListAdapter
    private lateinit var recommAdapter: AnimeRecommListAdapter
    private lateinit var relatedAdapter: AnimeRelatedListAdapter
    private lateinit var videoAdapter: AnimeVideoAdapter
    private lateinit var reviewAdapter: AnimeReviewListAdapter

    private lateinit var characterRecycler: RecyclerView
    private lateinit var staffRecycler: RecyclerView
    private lateinit var recommRecycler: RecyclerView
    private lateinit var relatedRecycler: RecyclerView
    private lateinit var videoRecycler: RecyclerView
    private lateinit var reviewRecycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            animeIdArg = AnimeDetailFragmentArgs.fromBundle(requireArguments()).animeId
            animeDetailViewModel.getAnimeDetail(animeIdArg)
            animeDetailViewModel.getAnimeCharacters(animeIdArg)
            animeDetailViewModel.getAnimeVideos(animeIdArg)
            animeDetailViewModel.getAnimeReviews(animeIdArg)

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        scoreCardView = binding.animeScoreFab
        scoreTextView = binding.animeScoreTxt

        characterRecycler = binding.root.characterRecycler
        characterRecycler.setHasFixedSize(true)
        staffRecycler = binding.root.staffRecycler
        staffRecycler.setHasFixedSize(true)
        recommRecycler = binding.root.recommRecycler
        recommRecycler.setHasFixedSize(true)
        relatedRecycler = binding.root.relatedAnimeRecycler
        relatedRecycler.setHasFixedSize(true)
        videoRecycler = binding.root.animeVideoRecycler
        videoRecycler.setHasFixedSize(true)
        reviewRecycler = binding.root.reviewsRecycler
        reviewRecycler.setHasFixedSize(true)

        toolbar = binding.animeDescToolbar

        toolbar.setNavigationOnClickListener {view->
            view.findNavController().navigateUp()

        }

        coverView = binding.root.anime_desc_cover_img

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        characterAdapter = AnimeCharacterListAdapter(AnimeCharacterListener {
            it?.let {
                navigateToCharacterDetails(it)

            }
        })
        staffAdapter = AnimeStaffListAdapter(AnimeStaffListener {

        })
        recommAdapter = AnimeRecommListAdapter(AnimeRecommListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        relatedAdapter = AnimeRelatedListAdapter(AnimeRecommListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        videoAdapter = AnimeVideoAdapter(AnimePromoListener {
            it?.let { openVideoLink(it) }
        })
        reviewAdapter = AnimeReviewListAdapter(AnimeReviewListener {

        })

        animeDetailViewModel.animeDetail.observe(viewLifecycleOwner) { animeEntity ->
            binding.animeEntity = animeEntity
            recommAdapter.submitList(animeEntity.recommendations)
            recommRecycler.apply {
                adapter = recommAdapter
            }
            relatedAdapter.submitList(animeEntity.relatedAnime)
            relatedRecycler.apply {
                adapter = relatedAdapter
            }


            animeEntity.mainPicture?.medium?.let {
                setScoreCardColor(it)
            }
            animeDetailViewModel.animeCharacterAndStaff.observe(viewLifecycleOwner) { characters ->
                characters?.let {
                    characterAdapter.submitList(it.characters)
                    staffAdapter.submitList(it.staff)

                    characterRecycler.adapter = characterAdapter

                    staffRecycler.adapter = staffAdapter

                }
            }

            animeDetailViewModel.animeVideosAndEpisodes.observe(viewLifecycleOwner) {
                it?.let { animeVideo ->
                    videoAdapter.submitList(animeVideo.promo)
                    videoRecycler.adapter = videoAdapter
                }
            }

            animeDetailViewModel.animeReview.observe(viewLifecycleOwner) {
                it?.let { animeReviews ->
                    reviewAdapter.submitList(animeReviews.reviews)
                    reviewRecycler.adapter = reviewAdapter
                }
            }


        }
    }

    private fun setScoreCardColor(imgUrl: String) {

        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    Palette.from(resource).generate { palette: Palette? ->
                        palette?.vibrantSwatch?.rgb?.let { color ->
                            scoreCardView.setCardBackgroundColor(color)
                        }

                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }


    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            AnimeDetailFragmentDirections.actionAnimeDetailFragmentSelf(animeMalId)
        )
    }


    private fun navigateToCharacterDetails(character: Int) {
        this.findNavController().navigate(
            AnimeDetailFragmentDirections.actionAnimeDetailFragmentToCharacterFragment(character)
        )
    }


    private fun openVideoLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}