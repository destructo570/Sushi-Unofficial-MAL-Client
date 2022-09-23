package com.destructo.sushi.ui.anime.animeDetails

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.destructo.sushi.*
import com.destructo.sushi.R
import com.destructo.sushi.adapter.*
import com.destructo.sushi.databinding.FragmentAnimeDetailBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus.*
import com.destructo.sushi.listener.*
import com.destructo.sushi.model.mal.common.Genre
import com.destructo.sushi.model.params.AnimeUpdateParams
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.AnimeUpdateDialog
import com.destructo.sushi.ui.anime.AnimeUpdateListener
import com.destructo.sushi.ui.base.BaseFragment
import com.destructo.sushi.util.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.inc_anime_alt_title.view.*
import kotlinx.android.synthetic.main.inc_anime_detail_sub_desc.view.*
import kotlinx.android.synthetic.main.inc_anime_episodes.view.*
import kotlinx.android.synthetic.main.inc_anime_videos.view.*
import kotlinx.android.synthetic.main.inc_characters_list.view.*
import kotlinx.android.synthetic.main.inc_genre_list.view.*
import kotlinx.android.synthetic.main.inc_more_anime_detail.view.*
import kotlinx.android.synthetic.main.inc_my_anime_status.view.*
import kotlinx.android.synthetic.main.inc_recomms_list.view.*
import kotlinx.android.synthetic.main.inc_related_anime.view.*
import kotlinx.android.synthetic.main.inc_review_list.view.*
import kotlinx.android.synthetic.main.inc_staff_list.view.*
import timber.log.Timber

private const val ANIME_IN_USER_LIST = 1
private const val ANIME_NOT_IN_USER_LIST = 0
private const val USER_ANIME_LIST_DEFAULT = -1
private const val IS_IN_USER_LIST_ARG = "isInUserList"

@AndroidEntryPoint
class AnimeDetailFragment : BaseFragment(),
    AppBarLayout.OnOffsetChangedListener, AnimeUpdateListener {

    private val animeDetailViewModel: AnimeDetailViewModel by viewModels()
    private val args: AnimeDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentAnimeDetailBinding

    private var animeIdArg: Int = 0
    private var isInUserList: Int = USER_ANIME_LIST_DEFAULT
    private var animeStatus: String? = null
    private var animeEpisodes: String? = null
    private var animeScore: Int? = 0
    private var animeStartDate: String? = null
    private var animeFinishDate: String? = null

    private lateinit var toolbar: Toolbar
    private lateinit var addToListButton: Button
    private lateinit var animeDetailProgressBar: ProgressBar
    private lateinit var myListStatus: View
    private lateinit var characterAdapter: AnimeCharacterListAdapter
    private lateinit var staffAdapter: AnimeStaffListAdapter
    private lateinit var recommAdapter: AnimeRecommListAdapter
    private lateinit var relatedAdapter: AnimeRelatedListAdapter
    private lateinit var videoAdapter: AnimeVideoAdapter
    private lateinit var reviewAdapter: AnimeReviewListAdapter
    private lateinit var episodeAdapter: AnimeEpisodePreviewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            animeIdArg = args.animeId
            isInUserList = savedInstanceState.getInt(IS_IN_USER_LIST_ARG)
        } else {
            animeIdArg = args.animeId
            animeDetailViewModel.getAnimeDetail(animeIdArg, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ANIME_ID_ARG, animeIdArg)
        outState.putInt(IS_IN_USER_LIST_ARG, isInUserList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimeDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        addToListButton = binding.root.add_anime_to_list
        myListStatus = binding.myAnimeStatus.root

        animeDetailProgressBar = binding.animeDetailProgress

        toolbar = binding.animeDescToolbar

        setupListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initialiseAdapters()

        animeDetailViewModel.animeDetail.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    animeDetailProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    animeDetailProgressBar.visibility = View.GONE
                    resources.data?.let { animeEntity ->
                        animeDetailViewModel.getAnimeCharacters(animeIdArg)
                        animeDetailViewModel.getAnimeVideos(animeIdArg)
                        animeDetailViewModel.getAnimeReviews(animeIdArg, "1")

                        binding.animeEntity = animeEntity

                        if (animeEntity.myAnimeListStatus != null) {
                            myListStatus.visibility = View.VISIBLE
                            isInUserList = ANIME_IN_USER_LIST
                            animeScore = animeEntity.myAnimeListStatus.score
                            animeStatus = animeEntity.myAnimeListStatus.status?.toTitleCase()
                            animeEpisodes =
                                animeEntity.myAnimeListStatus.numEpisodesWatched.toString()
                            animeStartDate = animeEntity.myAnimeListStatus.startDate
                            animeFinishDate = animeEntity.myAnimeListStatus.finishDate

                        } else {
                            isInUserList = ANIME_NOT_IN_USER_LIST
                        }

                        recommAdapter.submitList(animeEntity.recommendations)
                        relatedAdapter.submitList(animeEntity.relatedAnime)
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
                        episodeAdapter.submitList(animeVideo.episodeVideos)
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
                    }
                }
                Status.ERROR -> {
                    Timber.e("Error: %s", resource.message)
                }
            }
        }

        animeDetailViewModel.userAnimeStatus.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    changeButtonState(addToListButton, true)
                    isInUserList = ANIME_IN_USER_LIST
                    resource.data?.let { animeStatus ->
                        addToListButton.text = animeStatus.status.toString().toTitleCase()
                        addToListButton.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_check_fill,
                            0,
                            0,
                            0
                        )
                        myListStatus.visibility = View.VISIBLE
                        animeDetailViewModel.getAnimeDetail(animeIdArg, true)
                    }
                }
                Status.ERROR -> {
                    changeButtonState(addToListButton, true)
                }
            }
        }

        animeDetailViewModel.userAnimeRemove.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.ERROR -> {
                }
                Status.SUCCESS -> {
                    addToListButton.text = getString(R.string.add_to_list)
                    addToListButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_add_fill,
                        0,
                        0,
                        0
                    )
                    myListStatus.visibility = View.GONE
                    animeDetailViewModel.getAnimeDetail(animeIdArg, true)
                }
                Status.LOADING -> {

                }
            }
        }
    }

    private fun initialiseAdapters() {

        characterAdapter = AnimeCharacterListAdapter(AnimeCharacterListener {
            it?.let { navigateToCharacterDetails(it) }
        })
        staffAdapter = AnimeStaffListAdapter(AnimeStaffListener {
            it?.let { it.malId?.let { it1 -> navigateToPersonDetails(it1) } }
        })
        recommAdapter = AnimeRecommListAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        relatedAdapter = AnimeRelatedListAdapter(MalIdListener {
            it?.let { navigateToAnimeDetails(it) }
        })
        videoAdapter = AnimeVideoAdapter(AnimePromoListener {
            it?.let { context?.openUrl(it) }
        })
        reviewAdapter = AnimeReviewListAdapter(AnimeReviewListener {
            it?.let {
                val reviewDialog = AnimeReviewBottomSheetFragment.newInstance(it)
                reviewDialog.show(childFragmentManager, "anime_review_dialog")
            }
        })
        episodeAdapter = AnimeEpisodePreviewAdapter(MalUrlListener {
            it?.let {
                context?.openUrl(it)
            }
        })

        binding.root.characterRecycler.apply {
            addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
            setHasFixedSize(true)
            adapter = characterAdapter
        }
        binding.root.staffRecycler.apply {
            addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
            setHasFixedSize(true)
            adapter = staffAdapter

        }
        binding.root.recommRecycler.apply {
            addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
            setHasFixedSize(true)
            adapter = recommAdapter
        }
        binding.root.relatedAnimeRecycler.apply {
            addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
            setHasFixedSize(true)
            adapter = relatedAdapter
        }
        binding.root.animeVideoRecycler.apply {
            addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
            setHasFixedSize(true)
            adapter = videoAdapter
        }
        binding.root.animeEpisodeRecycler.apply {
            addItemDecoration(ListItemHorizontalDecor(LIST_SPACE_HEIGHT))
            setHasFixedSize(true)
            adapter = episodeAdapter
        }
        binding.root.reviewsRecycler.apply {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            adapter = reviewAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        binding.animeAppBar.addOnOffsetChangedListener(this)
    }

    private fun setupListeners() {
        setupToolbar()
        setAddToListClickListener()
        setMoreAnimeInfoClickListener()
        setNavigationListeners()
        setCollapseToolbarListener()
    }

    private fun setCollapseToolbarListener() {
        binding.animeCollapsingToolbar.setOnLongClickListener {
            copyAnimeTitleToClipBoard()
            return@setOnLongClickListener false
        }
    }

    private fun setNavigationListeners() {

        binding.root.charactersMore.setOnClickListener {
            findNavController().navigate(
                R.id.animeCharactersFragment,
                bundleOf(Pair(MAL_ID_ARG, animeIdArg)),
                getAnimNavOptions()
            )
        }
        binding.root.reviewsMore.setOnClickListener {
            findNavController().navigate(
                R.id.animeReviewsFragment,
                bundleOf(Pair(ANIME_ID_ARG, animeIdArg)),
                getAnimNavOptions()
            )
        }
        binding.root.staffMore.setOnClickListener {
            findNavController().navigate(
                R.id.allAnimeStaffFragment,
                bundleOf(Pair(MAL_ID_ARG, animeIdArg)),
                getAnimNavOptions()
            )
        }
        binding.root.episodesMore.setOnClickListener {
            findNavController().navigate(
                R.id.animeEpisodesFragment,
                bundleOf(Pair(ANIME_ID_ARG, animeIdArg)),
                getAnimNavOptions()
            )
        }
        binding.animeSongs.setOnClickListener {
            findNavController().navigate(
                R.id.animeSongsFragment,
                bundleOf(Pair(ANIME_ID_ARG, animeIdArg)),
                getAnimNavOptions()
            )
        }
    }


    private fun setGenreChips(genreList: List<Genre?>) {
        binding.root.genre_chip_group.removeAllViews()
        genreList.forEach { genre ->
            genre?.let {
                val chip = Chip(context, null, R.attr.customChipStyle)
                chip.text = it.name
                binding.root.genre_chip_group.addView(chip)
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

                        if (palette?.vibrantSwatch != null) {
                            palette.vibrantSwatch?.rgb?.let { color ->
                                binding.animeScoreFab.setCardBackgroundColor(color)
                            }
                            palette.vibrantSwatch?.titleTextColor?.let {
                                binding.animeScoreTxt.setTextColor(it)
                            }
                        } else {
                            context?.let {
                                binding.animeScoreFab.setCardBackgroundColor(it.getColorFromAttr(R.attr.scoreCardBackground))
                                binding.animeScoreTxt.setTextColor(it.getColorFromAttr(R.attr.scoreCardText))
                            }

                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun navigateToAnimeDetails(animeMalId: Int) {
        this.findNavController().navigate(
            R.id.animeDetailFragment,
            bundleOf(Pair(ANIME_ID_ARG, animeMalId)),
            getAnimNavOptions()
        )
    }

    private fun navigateToCharacterDetails(character: Int) {
        this.findNavController().navigate(
            R.id.characterFragment,
            bundleOf(Pair(CHARACTER_ID_ARG, character)),
            getAnimNavOptions()
        )
    }

    private fun navigateToPersonDetails(personId: Int) {
        this.findNavController().navigate(
            R.id.personFragment,
            bundleOf(Pair(PERSON_ID_ARG, personId)),
            getAnimNavOptions()
        )
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            var drawable: Drawable? = toolbar.navigationIcon

            drawable?.let {
                drawable = DrawableCompat.wrap(drawable!!)
                context?.let { it1 -> ContextCompat.getColor(it1, R.color.iconTintOnPrimary) }
                    ?.let { it2 ->
                        DrawableCompat.setTint(drawable!!.mutate(), it2)
                    }
                toolbar.navigationIcon = drawable
                toolbar.overflowIcon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_fill_light)
            }

        } else {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_line)
            toolbar.overflowIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_fill)

        }
    }

    override fun onUpdateClick(
        animeId: Int?,
        status: String,
        episodes: Int,
        score: Int,
        remove: Boolean,
        startDate: String?,
        finishDate: String?
    ) {

        if (!remove) {
            animeDetailViewModel.updateUserAnimeStatus(
                AnimeUpdateParams(
                    animeId = animeIdArg.toString(),
                    status = convertStatus(status),
                    num_watched_episodes = episodes,
                    score = score, start_date = startDate,
                    finish_date = finishDate,
                    totalEpisodes = animeDetailViewModel.animeDetail.value?.data?.numEpisodes
                )
            )
        } else {
            animeDetailViewModel.removeAnime(animeIdArg)
        }

    }

    private fun convertStatus(data: String): String {
        var status = ""

        when (data) {
            getString(R.string.watching) -> {
                status = WATCHING.value
            }
            getString(R.string.completed) -> {
                status = COMPLETED.value
            }
            getString(R.string.plan_to_watch) -> {
                status = PLAN_TO_WATCH.value
            }
            getString(R.string.dropped) -> {
                status = DROPPED.value
            }
            getString(R.string.on_hold) -> {
                status = ON_HOLD.value
            }
        }
        return status
    }

    private fun changeButtonState(button: Button, status: Boolean) {
        if (status) {
            button.isEnabled = true
            button.setTextColor(context?.let {
                AppCompatResources.getColorStateList(
                    it,
                    R.color.textColorOnPrimary
                )
            })
        } else {
            button.isEnabled = false
            button.setTextColor(context?.let {
                AppCompatResources.getColorStateList(
                    it,
                    R.color.textColorOnPrimary
                )
            })
        }
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
        toolbar.setOnLongClickListener {
            copyAnimeTitleToClipBoard()
            return@setOnLongClickListener false
        }
        toolbar.inflateMenu(R.menu.detail_menu_options)
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.share_item -> shareAnime()
                R.id.copy_title -> copyAnimeTitleToClipBoard()
                R.id.open_in_browser -> openInBrowser()
            }

            false
        }
    }

    private fun openInBrowser() {
        val url = BASE_MAL_ANIME_URL + animeIdArg
        context?.openUrl(url)
    }

    private fun shareAnime() {
        animeDetailViewModel.animeDetail.value?.data?.title?.let {
            val url = BASE_MAL_ANIME_URL + animeIdArg
            val data = String.format(getString(R.string.share_anime_or_manga), it, url)
            context?.shareText(data)
        }
    }

    private fun copyAnimeTitleToClipBoard() {
        animeDetailViewModel.animeDetail.value?.data?.title?.let {
            context?.copyToClipboard(it)
            context?.makeShortToast("${getString(R.string.copied_to_clipboard)}\n$it")
        }
    }

    private fun setMoreAnimeInfoClickListener() {
        binding.animeMoreInfo.setOnClickListener {
            if (it.anime_more_detail_view.visibility != View.VISIBLE) {
                it.anime_more_detail_view.visibility = View.VISIBLE
            } else {
                it.anime_more_detail_view.visibility = View.GONE
            }
        }
    }

    private fun setAddToListClickListener() {
        addToListButton.setOnClickListener {
            when (isInUserList) {
                USER_ANIME_LIST_DEFAULT -> {
                }
                ANIME_IN_USER_LIST -> {
                    AnimeUpdateDialog
                        .newInstance(
                            null, animeStatus, animeEpisodes,
                            animeScore ?: 0, animeStartDate, animeFinishDate
                        )
                        .show(childFragmentManager, "animeUpdateDialog")
                }
                ANIME_NOT_IN_USER_LIST -> {
                    changeButtonState(it as Button, false)
                    animeDetailViewModel.updateUserAnimeStatus(
                        AnimeUpdateParams(
                            animeId = animeIdArg.toString(),
                            status = PLAN_TO_WATCH.value
                        )
                    )
                }
            }
        }
    }

}