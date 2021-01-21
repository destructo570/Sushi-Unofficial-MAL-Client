package com.destructo.sushi.ui.anime.animeDetails

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.destructo.sushi.*
import com.destructo.sushi.adapter.*
import com.destructo.sushi.databinding.FragmentAnimeDetailBinding
import com.destructo.sushi.enum.mal.UserAnimeStatus
import com.destructo.sushi.enum.mal.UserAnimeStatus.*
import com.destructo.sushi.listener.*
import com.destructo.sushi.model.mal.common.Genre
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.anime.AnimeUpdateDialog
import com.destructo.sushi.ui.anime.AnimeUpdateListener
import com.destructo.sushi.util.getColorFromAttr
import com.destructo.sushi.util.toTitleCase
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.inc_anime_alt_title.view.*
import kotlinx.android.synthetic.main.inc_anime_detail_sub_desc.view.*
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
private const val ANIME_ID_ARG = "animeId"
private const val IS_IN_USER_LIST_ARG = "isInUserList"

@AndroidEntryPoint
class AnimeDetailFragment : Fragment(),
    AppBarLayout.OnOffsetChangedListener, AnimeUpdateListener {

    private val animeDetailViewModel: AnimeDetailViewModel by viewModels()
    private val args: AnimeDetailFragmentArgs by navArgs()

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
    private lateinit var animeTitleTextView:TextView
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

    private lateinit var characterMore:TextView
    private lateinit var reviewMore:TextView
    private lateinit var staffMore:TextView
    private lateinit var adView: AdView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            val animeIdArgument = args.animeId
            animeIdArg = animeIdArgument
            isInUserList = savedInstanceState.getInt(IS_IN_USER_LIST_ARG)
        }else{
            animeIdArg = AnimeDetailFragmentArgs.fromBundle(requireArguments()).animeId
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
        val snapHelper = PagerSnapHelper()
        reviewRecycler = binding.root.reviewsRecycler
        reviewRecycler.setHasFixedSize(true)
        snapHelper.attachToRecyclerView(reviewRecycler)
        characterMore = binding.root.charactersMore
        reviewMore = binding.root.reviewsMore
        staffMore = binding.root.staffMore


        toolbar = binding.animeDescToolbar
        appBar = binding.animeAppBar
        collapToolbar = binding.animeCollapsingToolbar
        collapToolbar.setOnLongClickListener {
            copyToClipBoard()
            return@setOnLongClickListener false
        }
        setupToolbar()
        setAddToListClickListener()
        setMoreAnimeInfoClickListener()
        setAnimeAltTitleClickListener()

        adView = binding.adView
        if(!SushiApplication.getContext().queryPurchases()){
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)

            adView.adListener = object: AdListener(){
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adView.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: Int) {
                    adView.visibility = View.GONE
                }
            }
        }

        characterMore.setOnClickListener {
            findNavController().navigate(R.id.allCharactersFragment, bundleOf(Pair("malId", animeIdArg)))
        }
        reviewMore.setOnClickListener {
            findNavController().navigate(R.id.allReviewsFragment, bundleOf(Pair("animeId", animeIdArg)))
        }
        staffMore.setOnClickListener {
            findNavController().navigate(R.id.allAnimeStaffFragment, bundleOf(Pair("malId", animeIdArg)))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
            it?.let { openUrl(it) }
        })
        reviewAdapter = AnimeReviewListAdapter(AnimeReviewListener {
            it?.let {
                val reviewDialog = AnimeReviewBottomSheetFragment.newInstance(it)
                reviewDialog.show(childFragmentManager, "anime_review_dialog")
            }

        })

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
                        animeDetailViewModel.getAnimeDetail(animeIdArg, true)
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
                    addToListButton.text = getString(R.string.add_to_list)
                    addToListButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_fill,0,0,0)
                    myListStatus.visibility = View.GONE
                    animeDetailViewModel.getAnimeDetail(animeIdArg, true)
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
                val chip = Chip(context, null, R.attr.customChipStyle)
                chip.text = it.name
                genreChipGroup.addView(chip)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appBar.addOnOffsetChangedListener(this)
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

                        if ( palette?.vibrantSwatch != null){
                            palette.vibrantSwatch?.rgb?.let { color ->
                                scoreCardView.setCardBackgroundColor(color)
                            }
                            palette.vibrantSwatch?.titleTextColor?.let {
                                scoreTextView.setTextColor(it)
                            }
                        }else{
                            context?.let {
                                scoreCardView.setCardBackgroundColor(it.getColorFromAttr(R.attr.scoreCardBackground))
                                scoreTextView.setTextColor(it.getColorFromAttr(R.attr.scoreCardText))
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
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, animeMalId))
        )
    }

    private fun navigateToCharacterDetails(character: Int) {
        this.findNavController().navigate(
            R.id.characterFragment, bundleOf(Pair(CHARACTER_ID_ARG, character))
        )
    }

    private fun navigateToPersonDetails(personId: Int) {
        this.findNavController().navigate(
            R.id.personFragment, bundleOf(Pair(PERSON_ID_ARG, personId))
        )
    }

    private fun openUrl(url: String) {
            val builder = CustomTabsIntent.Builder()
            val customTabIntent = builder.build()
            customTabIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    private fun shareUrl(url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        val title = animeDetailViewModel.animeDetail.value?.data?.title
        val data = "$title\n\n$url\n\nShared Using Sushi - MAL Client"
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, data)
        startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if(verticalOffset == 0){
            var drawable: Drawable? = toolbar.navigationIcon

            drawable?.let {
                drawable = DrawableCompat.wrap(drawable!!)
                context?.let { it1 -> ContextCompat.getColor(it1,R.color.iconTintOnPrimary) }?.let { it2 ->
                    DrawableCompat.setTint(drawable!!.mutate(), it2)
                }
                toolbar.navigationIcon = drawable
                toolbar.overflowIcon =  ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_fill_light)
            }

        }else{
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_line)
            toolbar.overflowIcon =  ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_fill)

        }
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
            getString(R.string.watching)->{status = WATCHING.value}
            getString(R.string.completed)->{status = COMPLETED.value}
            getString(R.string.plan_to_watch)->{status = PLAN_TO_WATCH.value}
            getString(R.string.dropped)->{status = DROPPED.value}
            getString(R.string.on_hold)->{status = ON_HOLD.value}
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

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
        toolbar.setOnLongClickListener {
            copyToClipBoard()
            return@setOnLongClickListener false
        }
        toolbar.inflateMenu(R.menu.detail_menu_options)
        toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                when(item?.itemId){
                    R.id.share_item ->{
                        val url = BASE_MAL_ANIME_URL + animeIdArg
                        shareUrl(url)
                    }
                    R.id.copy_title ->{
                        copyToClipBoard()
                    }
                    R.id.open_in_browser ->{
                        val url = BASE_MAL_ANIME_URL + animeIdArg
                        openUrl(url)
                    }
                }

                return false
            }

        })
    }

    private fun copyToClipBoard() {
        val title = animeDetailViewModel.animeDetail.value?.data?.title
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", title)
        clipboard.setPrimaryClip(clipData)

        Toast.makeText(context, "Copied to clipboard:\n$title", Toast.LENGTH_SHORT).show()
    }

    private fun setAnimeAltTitleClickListener() {
        animeAltTitleLayout.setOnClickListener {
            if (it.anime_alt_title_view.visibility != View.VISIBLE) {
                it.anime_alt_title_view.visibility = View.VISIBLE
            } else {
                it.anime_alt_title_view.visibility = View.GONE
            }
        }
    }

    private fun setMoreAnimeInfoClickListener() {
        moreAnimeInfoLayout.setOnClickListener {
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
                    val myDialog = AnimeUpdateDialog
                        .newInstance(null, animeStatus, animeEpisodes, animeScore ?: 0)
                    myDialog.show(childFragmentManager, "animeUpdateDialog")
                }
                ANIME_NOT_IN_USER_LIST -> {
                    changeButtonState(it as Button, false)
                    animeDetailViewModel.updateUserAnimeStatus(
                        animeId = animeIdArg.toString(),
                        status = UserAnimeStatus.PLAN_TO_WATCH.value)
                }
            }
        }
    }



}