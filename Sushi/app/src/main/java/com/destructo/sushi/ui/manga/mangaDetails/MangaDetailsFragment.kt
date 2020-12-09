package com.destructo.sushi.ui.manga.mangaDetails

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
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.destructo.sushi.R
import com.destructo.sushi.adapter.MangaCharacterAdapter
import com.destructo.sushi.adapter.MangaRecommListAdapter
import com.destructo.sushi.adapter.MangaRelatedListAdapter
import com.destructo.sushi.adapter.MangaReviewAdapter
import com.destructo.sushi.databinding.FragmentMangaDetailsBinding
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.listener.MangaCharacterListener
import com.destructo.sushi.listener.MangaReviewListener
import com.destructo.sushi.model.mal.common.Genre
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.manga.MangaUpdateDialog
import com.destructo.sushi.ui.manga.MangaUpdateListener
import com.destructo.sushi.util.toTitleCase
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_manga_details.view.*
import kotlinx.android.synthetic.main.inc_characters_list.view.*
import kotlinx.android.synthetic.main.inc_genre_list.view.*
import kotlinx.android.synthetic.main.inc_manga_alt_title.view.*
import kotlinx.android.synthetic.main.inc_manga_sub_desc.view.*
import kotlinx.android.synthetic.main.inc_more_manga_detail.view.*
import kotlinx.android.synthetic.main.inc_recomms_list.view.*
import kotlinx.android.synthetic.main.inc_related_manga.view.*
import kotlinx.android.synthetic.main.inc_review_list.view.*
import timber.log.Timber
import java.util.*

private const val MANGA_IN_USER_LIST = 1
private const val MANGA_NOT_IN_USER_LIST = 0
private const val USER_MANGA_LIST_DEFAULT = -1

@AndroidEntryPoint
class MangaDetailsFragment : Fragment(), MangaUpdateListener, AppBarLayout.OnOffsetChangedListener {

    private val mangaDetailViewModel: MangaDetailViewModel by viewModels()
    private lateinit var binding: FragmentMangaDetailsBinding
    private var mangaIdArg: Int = 0

    private lateinit var toolbar: Toolbar
    private lateinit var appBar: AppBarLayout
    private lateinit var collapToolbar: CollapsingToolbarLayout
    private lateinit var scoreCardView: MaterialCardView
    private lateinit var coverView: ImageView
    private lateinit var scoreTextView: TextView
    private lateinit var genreChipGroup: ChipGroup
    private lateinit var myListStatus: LinearLayout
    private lateinit var addToListButton:Button
    private lateinit var mangaDetailProgressBar:ProgressBar
    private var isInUserList:Int = USER_MANGA_LIST_DEFAULT
    private lateinit var mangaMoreInfoLayout:ConstraintLayout
    private lateinit var mangaAltTitleLayout:ConstraintLayout


    private lateinit var characterAdapter: MangaCharacterAdapter
    private lateinit var relatedAdapter: MangaRelatedListAdapter
    private lateinit var recommAdapter: MangaRecommListAdapter
    private lateinit var reviewAdapter: MangaReviewAdapter

    private var mangaStatus:String?=null
    private var mangaChapters:String?=null
    private var mangaVolumes:String?=null
    private var mangaScore:Int?=0

    private lateinit var characterRecycler: RecyclerView
    private lateinit var relatedRecycler: RecyclerView
    private lateinit var recommRecycler: RecyclerView
    private lateinit var reviewRecycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mangaIdArg = savedInstanceState.getInt("mangaId")
            isInUserList = savedInstanceState.getInt("isInUserList")
        }else{
            mangaIdArg = MangaDetailsFragmentArgs.fromBundle(requireArguments()).mangaId
            mangaDetailViewModel.getMangaDetail(mangaIdArg, false)
//            mangaDetailViewModel.getMangaCharacters(mangaIdArg)
//            mangaDetailViewModel.getMangaReviews(mangaIdArg)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("mangaId", mangaIdArg)
        outState.putInt("isInUserList", isInUserList)
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
        myListStatus = binding.root.my_manga_status
        addToListButton = binding.root.add_manga_to_list
        mangaDetailProgressBar = binding.root.manga_detail_progress
        mangaMoreInfoLayout = binding.root.manga_more_detail_layout
        mangaAltTitleLayout = binding.root.manga_alt_title_layout

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

        addToListButton.setOnClickListener {

            when(isInUserList){
                USER_MANGA_LIST_DEFAULT ->{
                    Timber.e("DO nothing case...")
                }
                MANGA_IN_USER_LIST ->{
                    Timber.e("Open Modal Dialog")
                    val myDialog = MangaUpdateDialog.newInstance(mangaStatus,mangaChapters,mangaVolumes,mangaScore?:0)
                    myDialog.show(childFragmentManager, "mangaUpdateDialog")
                }
                MANGA_NOT_IN_USER_LIST ->{
                    Timber.e("Adding to list...")
                    mangaDetailViewModel.updateUserMangaStatus(
                        mangaId = mangaIdArg.toString(), status = UserMangaStatus.READING.value)
                }
            }

        }

        mangaMoreInfoLayout.setOnClickListener {
            if(it.manga_more_detail_view.visibility != View.VISIBLE){
                it.manga_more_detail_view.visibility = View.VISIBLE
            }else{
                it.manga_more_detail_view.visibility = View.GONE
            }
        }

        mangaAltTitleLayout.setOnClickListener {
            if(it.manga_alt_title_view.visibility != View.VISIBLE){
                it.manga_alt_title_view.visibility = View.VISIBLE
            }else{
                it.manga_alt_title_view.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recommAdapter = MangaRecommListAdapter(MalIdListener {
            it?.let { navigateToMangaDetails(it) }
        })

        relatedAdapter = MangaRelatedListAdapter(MalIdListener {
            it?.let { navigateToMangaDetails(it) }
        })

        characterAdapter = MangaCharacterAdapter(MangaCharacterListener {
            it?.let { it.malId?.let { it1 -> navigateToCharacterDetails(it1) } }

        })

        reviewAdapter = MangaReviewAdapter(MangaReviewListener {
            it?.let { it.url?.let { it1 -> openUrl(it1) } }
            it?.let {
                val reviewDialog = MangaReviewBottomSheetFragment.newInstance(it)
                reviewDialog.show(childFragmentManager, "manga_review_dialog")
            }
        })


        mangaDetailViewModel.mangaDetail.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                    mangaDetailProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    mangaDetailProgressBar.visibility = View.GONE
                    resource.data?.let { manga ->
                        mangaDetailViewModel.getMangaCharacters(mangaIdArg)
                        mangaDetailViewModel.getMangaReviews(mangaIdArg)

                        binding.mangaEntity = manga

                        if (manga.myMangaListStatus != null) {
                            myListStatus.visibility = View.VISIBLE
                            isInUserList = MANGA_IN_USER_LIST
                            mangaScore = manga.myMangaListStatus.score
                            mangaStatus =  manga.myMangaListStatus.status?.toTitleCase()
                            mangaChapters =  manga.myMangaListStatus.numChaptersRead.toString()
                            mangaVolumes =  manga.myMangaListStatus.numVolumesRead.toString()
                        }else {
                            isInUserList = MANGA_NOT_IN_USER_LIST
                        }

                        manga.mainPicture?.medium?.let {
                            setScoreCardColor(it)
                        }

                        manga.genres?.let { setGenreChips(it) }

                        recommAdapter.submitList(manga.recommendations)
                        relatedAdapter.submitList(manga.relatedManga)
                        recommRecycler.adapter = recommAdapter
                        relatedRecycler.adapter = relatedAdapter

                    }
                }
                Status.ERROR -> {
                }
            }
        }

        mangaDetailViewModel.mangaCharacter.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.let { characters ->
                        characterAdapter.submitList(characters.characters)
                        characterRecycler.adapter = characterAdapter
                    }
                }
                Status.ERROR -> {
                }
            }
        }

        mangaDetailViewModel.mangaReview.observe(viewLifecycleOwner) { resource ->

            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.let { mangaReview ->
                        reviewAdapter.submitList(mangaReview.reviews)
                        reviewRecycler.adapter = reviewAdapter
                    }
                }
                Status.ERROR -> {
                }
            }
        }

        mangaDetailViewModel.userMangaStatus.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING->{}
                Status.SUCCESS->{
                    changeButtonState(addToListButton, true)
                    isInUserList = MANGA_IN_USER_LIST
                    resource.data?.let {mangaStatus->
                        addToListButton.text = titleCaseString(mangaStatus.status.toString())
                        addToListButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_fill,0,0,0)
                        myListStatus.visibility = View.VISIBLE
                        mangaDetailViewModel.getMangaDetail(mangaIdArg, true)
                    }
                }
                Status.ERROR->{
                    changeButtonState(addToListButton, true)
                }
            }
        }

        mangaDetailViewModel.userMangaRemove.observe(viewLifecycleOwner){
            when(it.status){
                Status.ERROR->{}
                Status.SUCCESS -> {
                    addToListButton.text = getString(R.string.anime_detail_add_to_list)
                    addToListButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_fill,0,0,0)
                    myListStatus.visibility = View.GONE
                    mangaDetailViewModel.getMangaDetail(mangaIdArg, true)
                }
                Status.LOADING -> {

                }
            }
        }
    }
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
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

    private fun navigateToCharacterDetails(character: Int) {
        this.findNavController().navigate(
            MangaDetailsFragmentDirections.actionMangaDetailsFragmentToCharacterFragment(character)
        )
    }

    private fun titleCaseString(data: String):String{
        val str = data.replace("_", " ", true)
        val words = str.split(" ")
        var finalString = ""
        words.forEach {
            finalString += it.capitalize(Locale.ROOT) + " "
        }
        return finalString
    }

    private fun convertStatus(data: String): String{
        var status = ""

        when(data){
            getString(R.string.user_manga_status_reading)->{status = UserMangaStatus.READING.value}
            getString(R.string.user_manga_status_completed)->{status = UserMangaStatus.COMPLETED.value}
            getString(R.string.user_manga_status_plan_to_read)->{status = UserMangaStatus.PLAN_TO_READ.value}
            getString(R.string.user_manga_status_dropped)->{status = UserMangaStatus.DROPPED.value}
            getString(R.string.user_manga_status_on_hold)->{status = UserMangaStatus.ON_HOLD.value}
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

    override fun onUpdateClick(
        status: String,
        chapters: Int,
        volume: Int,
        score: Int,
        remove: Boolean
    ) {
        if (!remove){
            mangaDetailViewModel.updateUserMangaStatus(
                mangaId = mangaIdArg.toString(),
                status = convertStatus(status),
                num_chapters_read = chapters,
                num_volumes_read = volume,
                score = score)
        }else{
            mangaDetailViewModel.removeAnime(mangaIdArg)
        }
    }

    override fun onResume() {
        super.onResume()
        appBar.addOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if(verticalOffset == 0){
            var drawable: Drawable? = toolbar.navigationIcon
            drawable?.let {
                drawable = DrawableCompat.wrap(drawable!!)
                context?.let { it1 -> ContextCompat.getColor(it1,R.color.iconTintOnPrimary) }?.let { it2 ->
                    DrawableCompat.setTint(drawable!!.mutate(),
                        it2
                    )
                }
                toolbar.navigationIcon = drawable
            }

        }else{
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_line)
        }
    }


}