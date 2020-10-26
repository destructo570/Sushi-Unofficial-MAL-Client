package com.destructo.sushi.ui.manga.mangaDetails

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeDetailBinding
import com.destructo.sushi.databinding.FragmentMangaDetailsBinding
import com.destructo.sushi.ui.anime.adapter.AnimeCharacterListAdapter
import com.destructo.sushi.ui.anime.adapter.AnimeRecommListAdapter
import com.destructo.sushi.ui.anime.adapter.AnimeStaffListAdapter
import com.destructo.sushi.ui.anime.animeDetails.AnimeDetailFragmentArgs
import com.destructo.sushi.ui.anime.animeDetails.AnimeDetailFragmentDirections
import com.destructo.sushi.ui.manga.adapter.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_character_voice_actors.view.*
import kotlinx.android.synthetic.main.inc_characters_list.view.*
import kotlinx.android.synthetic.main.inc_genre_list.view.*
import kotlinx.android.synthetic.main.inc_manga_sub_desc.view.*
import kotlinx.android.synthetic.main.inc_recomms_list.view.*
import kotlinx.android.synthetic.main.inc_related_manga.view.*
import kotlinx.android.synthetic.main.inc_review_list.view.*

@AndroidEntryPoint
class MangaDetailsFragment : Fragment() {


    private val mangaDetailViewModel:MangaDetailViewModel by viewModels()
    private lateinit var binding:FragmentMangaDetailsBinding
    private var mangaIdArg:Int=0

    private lateinit var toolbar: Toolbar
    private lateinit var appBar: AppBarLayout
    private lateinit var collapToolbar: CollapsingToolbarLayout
    private lateinit var scoreCardView: MaterialCardView
    private lateinit var coverView: ImageView
    private lateinit var scoreTextView: TextView
    private lateinit var genreChipGroup: ChipGroup
    private lateinit var myListStatus: LinearLayout

    private lateinit var characterAdapter: MangaCharacterAdapter
    private lateinit var relatedAdapter: MangaRelatedListAdapter
    private lateinit var recommAdapter: MangaRecommListAdapter
    private lateinit var reviewAdapter: MangaReviewAdapter

    private lateinit var characterRecycler: RecyclerView
    private lateinit var relatedRecycler: RecyclerView
    private lateinit var recommRecycler: RecyclerView
    private lateinit var reviewRecycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            mangaIdArg = MangaDetailsFragmentArgs.fromBundle(requireArguments()).mangaId
            mangaDetailViewModel.getMangaDetail(mangaIdArg)
            mangaDetailViewModel.getMangaCharacters(mangaIdArg)
            mangaDetailViewModel.getMangaReviews(mangaIdArg)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentMangaDetailsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        characterRecycler = binding.root.characterRecycler
        reviewRecycler = binding.root.reviewsRecycler
        relatedRecycler = binding.root.relatedMangaRecycler
        recommRecycler = binding.root.recommRecycler


        toolbar = binding.mangaDescToolbar
        appBar = binding.mangaAppBar
        collapToolbar = binding.mangaCollapsingToolbar
        scoreCardView = binding.mangaScoreFab
        scoreTextView = binding.mangaScoreTxt
        coverView = binding.root.manga_desc_cover_img
        genreChipGroup = binding.root.genre_chip_group

        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recommAdapter = MangaRecommListAdapter(MangaRecommListener {
            it?.let { navigateToMangaDetails(it) }
        })

        relatedAdapter = MangaRelatedListAdapter(MangaRecommListener {
            it?.let { navigateToMangaDetails(it) }
        })

        characterAdapter = MangaCharacterAdapter(MangaCharacterListener {

        })

        reviewAdapter = MangaReviewAdapter(MangaReviewListener {

        })

        mangaDetailViewModel.mangaDetail.observe(viewLifecycleOwner){manga->
            binding.mangaEntity = manga

            manga.mainPicture?.medium?.let {
                setScoreCardColor(it)
            }

            manga.genres?.forEach { genre ->
                genre?.let {
                    val chip = Chip(context)
                    chip.text = it.name
                    chip.isClickable = false
                    chip.setTextAppearance(R.style.TextAppearance_Sushi_ByLine2)
                    chip.chipBackgroundColor =
                        context?.let { it1 -> AppCompatResources.getColorStateList(it1, R.color.chip_bg_color) }
                    genreChipGroup.addView(chip)
                }
            }

            recommAdapter.submitList(manga.recommendations)
            relatedAdapter.submitList(manga.relatedManga)
            recommRecycler.adapter = recommAdapter
            relatedRecycler.adapter = relatedAdapter

        }


        mangaDetailViewModel.mangaCharacter.observe(viewLifecycleOwner){characters->
            characterAdapter.submitList(characters.characters)
            characterRecycler.adapter = characterAdapter
        }

        mangaDetailViewModel.mangaReview.observe(viewLifecycleOwner){mangaReview->
            reviewAdapter.submitList(mangaReview.reviews)
            reviewRecycler.adapter = reviewAdapter
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
                        palette?.vibrantSwatch?.titleTextColor?.let {
                            scoreTextView.setTextColor(it)
                        }

                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }


    private fun navigateToMangaDetails(malId: Int) {
        this.findNavController().navigate(
            MangaDetailsFragmentDirections.actionMangaDetailsFragmentSelf(malId)
        )
    }

}