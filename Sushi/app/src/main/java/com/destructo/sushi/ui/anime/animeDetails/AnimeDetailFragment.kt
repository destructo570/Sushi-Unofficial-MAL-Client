package com.destructo.sushi.ui.anime.animeDetails

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeDetailBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.enum.mal.UserAnimeStatus.*
import com.destructo.sushi.model.mal.common.Genre
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.AnimeUpdateDialog
import com.destructo.sushi.ui.anime.AnimeUpdateDialog.*
import com.destructo.sushi.ui.anime.AnimeUpdateListener
import com.destructo.sushi.ui.anime.adapter.*
import com.destructo.sushi.ui.anime.listener.*
import com.destructo.sushi.util.toTitleCase
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_anime_detail.*
import kotlinx.android.synthetic.main.inc_anime_alt_title.view.*
import kotlinx.android.synthetic.main.inc_anime_detail_sub_desc.view.*
import kotlinx.android.synthetic.main.inc_anime_videos.view.*
import kotlinx.android.synthetic.main.inc_characters_list.view.*
import kotlinx.android.synthetic.main.inc_genre_list.view.*
import kotlinx.android.synthetic.main.inc_more_anime_detail.view.*
import kotlinx.android.synthetic.main.inc_my_anime_status.view.*
import kotlinx.android.synthetic.main.inc_opening_ending_song_detail.view.*
import kotlinx.android.synthetic.main.inc_opening_ending_song_detail.view.anime_opening_ending
import kotlinx.android.synthetic.main.inc_recomms_list.view.*
import kotlinx.android.synthetic.main.inc_related_anime.view.*
import kotlinx.android.synthetic.main.inc_review_list.view.*
import kotlinx.android.synthetic.main.inc_staff_list.view.*
import timber.log.Timber
import java.util.*

private const val ANIME_IN_USER_LIST = 1
private const val ANIME_NOT_IN_USER_LIST = 0
private const val USER_ANIME_LIST_DEFAULT = -1

@AndroidEntryPoint
class AnimeDetailFragment : Fragment(),
    AppBarLayout.OnOffsetChangedListener, AnimeUpdateListener {

    private val animeDetailViewModel: AnimeDetailViewModel by viewModels()

    private lateinit var binding: FragmentAnimeDetailBinding
    private var animeIdArg: Int = 0
    private lateinit var scoreCardView: MaterialCardView
    private lateinit var coverView: ImageView
    private lateinit var scoreTextView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var appBar: AppBarLayout
    private lateinit var collapToolbar: CollapsingToolbarLayout
    private lateinit var genreChipGroup: ChipGroup
    private lateinit var addToListButton:Button
    private lateinit var animeDetailProgressBar: ProgressBar
    private var isInUserList:Int = USER_ANIME_LIST_DEFAULT

    private lateinit var myListStatus: LinearLayout
    private lateinit var myListScore:TextView
    private lateinit var myListEpisode:TextView
    private lateinit var myListCurrentStatus:TextView
    private lateinit var myListRewatching:TextView
    private lateinit var animeSongsLayout: ConstraintLayout
    private lateinit var moreAnimeInfoLayout: ConstraintLayout
    private lateinit var animeAltTitleLayout: ConstraintLayout


    private var animeStatus:String?=null
    private var animeEpisodes:String?=null
    private var animeScore:Int?=0

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

        if (savedInstanceState != null) {
            animeIdArg = savedInstanceState.getInt("animeId")
            isInUserList = savedInstanceState.getInt("isInUserList")
        }else{
            animeIdArg = AnimeDetailFragmentArgs.fromBundle(requireArguments()).animeId
            animeDetailViewModel.getAnimeDetail(animeIdArg)
            animeDetailViewModel.getAnimeCharacters(animeIdArg)
            animeDetailViewModel.getAnimeVideos(animeIdArg)
            animeDetailViewModel.getAnimeReviews(animeIdArg)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("animeId", animeIdArg)
        outState.putInt("isInUserList", isInUserList)
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
        coverView = binding.root.anime_desc_cover_img
        genreChipGroup = binding.root.genre_chip_group
        addToListButton = binding.root.add_anime_to_list

        myListStatus = binding.myAnimeStatus
        myListCurrentStatus = binding.root.user_anime_status_text
        myListEpisode = binding.root.user_anime_episode_text
        myListScore = binding.root.user_anime_score_text
        myListRewatching = binding.root.user_anime_rewatching_text
        animeDetailProgressBar = binding.animeDetailProgress
        animeSongsLayout = binding.root.anime_opening_ending
        moreAnimeInfoLayout = binding.root.anime_more_detail
        animeAltTitleLayout = binding.root.anime_alt_title_layout

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
        appBar = binding.animeAppBar
        collapToolbar = binding.animeCollapsingToolbar

        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()

        }

        addToListButton.setOnClickListener {

            when(isInUserList){
                USER_ANIME_LIST_DEFAULT->{
                }
                ANIME_IN_USER_LIST->{
                    val myDialog = AnimeUpdateDialog
                        .newInstance(null, animeStatus,animeEpisodes,animeScore ?: 0)
                    myDialog.show(childFragmentManager, "animeUpdateDialog")
                }
                ANIME_NOT_IN_USER_LIST->{
                    changeButtonState(it as Button,false)
                    animeDetailViewModel.updateUserAnimeStatus(animeId = animeIdArg.toString())
                }
            }
        }

        animeSongsLayout.setOnClickListener {
            if(it.anime_ost_view.visibility != View.VISIBLE){
                    it.anime_ost_view.visibility = View.VISIBLE
                }else{
                it.anime_ost_view.visibility = View.GONE
            }
        }

        moreAnimeInfoLayout.setOnClickListener {
            if(it.anime_more_detail_view.visibility != View.VISIBLE){
                it.anime_more_detail_view.visibility = View.VISIBLE
            }else{
                it.anime_more_detail_view.visibility = View.GONE
            }
        }
        animeAltTitleLayout.setOnClickListener {
            if(it.anime_alt_title_view.visibility != View.VISIBLE){
                it.anime_alt_title_view.visibility = View.VISIBLE
            }else{
                it.anime_alt_title_view.visibility = View.GONE
            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        characterAdapter = AnimeCharacterListAdapter(AnimeCharacterListener {
            it?.let { navigateToCharacterDetails(it) }
        })
        staffAdapter = AnimeStaffListAdapter(AnimeStaffListener {

        })
        recommAdapter = AnimeRecommListAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        relatedAdapter = AnimeRelatedListAdapter(AnimeIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        videoAdapter = AnimeVideoAdapter(AnimePromoListener {
            it?.let { openVideoLink(it) }
        })
        reviewAdapter = AnimeReviewListAdapter(AnimeReviewListener {

        })


        animeDetailViewModel.animeDetail.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    animeDetailProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    animeDetailProgressBar.visibility = View.GONE
                    resources.data?.let { animeEntity ->

                        binding.animeEntity = animeEntity

                        if (animeEntity.myAnimeListStatus != null) {
                            myListStatus.visibility = View.VISIBLE
                            isInUserList = ANIME_IN_USER_LIST
                            animeScore = animeEntity.myAnimeListStatus.score
                            animeStatus = animeEntity.myAnimeListStatus.status?.toTitleCase()
                            animeEpisodes = animeEntity.myAnimeListStatus.numEpisodesWatched.toString()
                        }else {
                            isInUserList = ANIME_NOT_IN_USER_LIST
                        }

                        recommAdapter.submitList(animeEntity.recommendations)
                        relatedAdapter.submitList(animeEntity.relatedAnime)
                        recommRecycler.adapter = recommAdapter
                        relatedRecycler.adapter = relatedAdapter

                        animeEntity.genres?.let { setGenreChips(it) }

                        animeEntity.mainPicture?.medium?.let { setScoreCardColor(it) }

                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resources.message)
                }
            }

        }
        animeDetailViewModel.animeCharacterAndStaff.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resources.data?.let {

                        characterAdapter.submitList(it.characters)
                        staffAdapter.submitList(it.staff)
                        characterRecycler.adapter = characterAdapter
                        staffRecycler.adapter = staffAdapter

                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resources.message)
                }
            }

        }

        animeDetailViewModel.animeVideosAndEpisodes.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.let { animeVideo ->
                        videoAdapter.submitList(animeVideo.promo)
                        videoRecycler.adapter = videoAdapter
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }

        }


        animeDetailViewModel.animeReview.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.let { animeReviews ->
                        reviewAdapter.submitList(animeReviews.reviews)
                        reviewRecycler.adapter = reviewAdapter
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }

            }
        }

        animeDetailViewModel.userAnimeStatus.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING->{
                }
                Status.SUCCESS->{
                    changeButtonState(addToListButton, true)
                    isInUserList = ANIME_IN_USER_LIST
                    resource.data?.let {animeStatus->
                        addToListButton.text = animeStatus.status.toString().toTitleCase()
                        addToListButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_fill,0,0,0)
                        myListStatus.visibility = View.VISIBLE
                        animeDetailViewModel.getAnimeDetail(animeIdArg)
                    }
                }
                Status.ERROR->{
                    changeButtonState(addToListButton, true)
                }
            }
        }

        animeDetailViewModel.userAnimeRemove.observe(viewLifecycleOwner){
            when(it.status){
                Status.ERROR->{}
                Status.SUCCESS -> {
                    addToListButton.text = getString(R.string.anime_detail_add_to_list)
                    addToListButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_fill,0,0,0)
                    myListStatus.visibility = View.GONE
                    animeDetailViewModel.getAnimeDetail(animeIdArg)
                }
                Status.LOADING -> {

                }
            }
        }

    }

    private fun setGenreChips(genreList: List<Genre?>) {
        genreChipGroup.removeAllViews()
        genreList.forEach { genre ->
            genre?.let {
                val chip = Chip(context)
                chip.text = it.name
                chip.isClickable = false
                chip.setTextAppearance(R.style.TextAppearance_Sushi_ByLine2)
                chip.chipBackgroundColor =
                    context?.let { it1 ->
                        AppCompatResources.getColorStateList(
                            it1,
                            R.color.chip_bg_color
                        )
                    }
                genreChipGroup.addView(chip)
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
                        palette?.vibrantSwatch?.titleTextColor?.let {
                            scoreTextView.setTextColor(it)
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

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

    }

    override fun onUpdateClick(
        animeId: Int?,
        status: String,
        episodes: Int,
        score: Int,
        remove: Boolean
    ) {

        if (!remove){
            animeDetailViewModel.updateUserAnimeStatus(
                animeId = animeIdArg.toString(),
                status = convertStatus(status),
                num_watched_episodes = episodes,
                score = score)
        }else{
            animeDetailViewModel.removeAnime(animeIdArg)
        }

    }

    private fun convertStatus(data: String): String{
        var status = ""

        when(data){
            getString(R.string.user_anime_status_watching)->{status = WATCHING.value}
            getString(R.string.user_anime_status_completed)->{status = COMPLETED.value}
            getString(R.string.user_anime_status_plan_to_watch)->{status = PLAN_TO_WATCH.value}
            getString(R.string.user_anime_status_dropped)->{status = DROPPED.value}
            getString(R.string.user_anime_status_on_hold)->{status = ON_HOLD.value}
        }
        return status
    }

    private fun changeButtonState(button: Button, status: Boolean){
        if (status){
            button.isEnabled = true
            button.setTextColor(context?.let { AppCompatResources.getColorStateList(it,R.color.textColorOnPrimary) })
        }else{
            button.isEnabled = false
            button.setTextColor(context?.let { AppCompatResources.getColorStateList(it,R.color.textColorOnPrimary) })
        }
    }

}