package com.destructo.sushi.ui.common.characterDetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.BASE_MAL_CHARACTER_URL
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentCharacterBinding
import com.destructo.sushi.util.copyToClipboard
import com.destructo.sushi.util.makeShortToast
import com.destructo.sushi.util.openUrl
import com.destructo.sushi.util.shareText
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterPager: ViewPager2
    private lateinit var characterPagerAdapter: FragmentStateAdapter
    private lateinit var characterTabLayout: TabLayout
    private var characterArg: Int = 0
    private lateinit var characterTabMediator:TabLayoutMediator
    private lateinit var toolbar: Toolbar
    private lateinit var appBar: AppBarLayout
    private lateinit var collapToolbar:CollapsingToolbarLayout


    private val characterViewModel: CharacterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            characterArg = CharacterFragmentArgs.fromBundle(requireArguments()).characterId
            characterViewModel.getCharacterDetails(characterArg)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterBinding
            .inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        toolbar = binding.toolbar
        characterPager = binding.characterViewPager
        characterTabLayout = binding.characterTabLayout
        appBar = binding.appbar
        collapToolbar = binding.animeCollapsingToolbar
        collapToolbar.setOnLongClickListener {
            copyCharacterNameToClipBoard()
            return@setOnLongClickListener false
        }

        characterTabMediator = TabLayoutMediator(characterTabLayout, characterPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.about)
                }
                1 -> {
                    tab.text = getString(R.string.voice_actor)
                }
                2 -> {
                    tab.text = getString(R.string.animeography)
                }
                3 -> {
                    tab.text = getString(R.string.mangaography)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        characterViewModel.character.observe(viewLifecycleOwner) { character ->
            binding.characterEntity = character
            val about = character.about as String

            val fragmentList = arrayListOf(
                CharacterAbout.newInstance(about),
                CharacterVoiceActors(),
                CharacterAnimeography(),
                CharacterMangaography()
            )
            setupViewPager(fragmentList)
        }
    }

    override fun onResume() {
        super.onResume()
        appBar.addOnOffsetChangedListener(this)
    }

    private fun setupViewPager(fragmentList:ArrayList<Fragment>){
        characterPagerAdapter = CharacterPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )
        characterPager.adapter = characterPagerAdapter
        characterTabMediator.attach()

    }

    fun setupToolbar(){
        toolbar.setOnLongClickListener {
            copyCharacterNameToClipBoard()
            return@setOnLongClickListener false
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        toolbar.inflateMenu(R.menu.detail_menu_options)
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.share_item -> shareCharacter()
                R.id.copy_title -> copyCharacterNameToClipBoard()
                R.id.open_in_browser -> openInBrowser()
            }

            false
        }
    }

    private fun openInBrowser() {
        val url = BASE_MAL_CHARACTER_URL + characterArg
        context?.openUrl(url)
    }

    private fun shareCharacter() {
        characterViewModel.character.value?.name?.let {
            val url = BASE_MAL_CHARACTER_URL + characterArg
            val data = String.format(getString(R.string.share_anime_or_manga), it, url)
            context?.shareText(data)
        }
    }

    private fun copyCharacterNameToClipBoard() {
        characterViewModel.character.value?.name?.let{
            context?.copyToClipboard(it)
            context?.makeShortToast("${getString(R.string.copied_to_clipboard)}\n$it")
        }

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
                toolbar.overflowIcon =  ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_fill_light)
            }

        }else{
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_line)
            toolbar.overflowIcon =  ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_fill)
        }
    }




}

