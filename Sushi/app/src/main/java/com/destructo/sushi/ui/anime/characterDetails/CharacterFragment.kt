package com.destructo.sushi.ui.anime.characterDetails

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment() : Fragment(), AppBarLayout.OnOffsetChangedListener {

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
    ): View? {
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
            copyToClipBoard()
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
            copyToClipBoard()
            return@setOnLongClickListener false
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        toolbar.inflateMenu(R.menu.detail_menu_options)
        toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                when(item?.itemId){
                    R.id.share_item ->{
                        val url = BASE_MAL_CHARACTER_URL + characterArg
                        shareUrl(url)
                    }
                    R.id.copy_title ->{
                        copyToClipBoard()
                    }
                    R.id.open_in_browser ->{
                        val url = BASE_MAL_CHARACTER_URL + characterArg
                        openUrl(url)
                    }
                }

                return false
            }

        })
    }

    private fun copyToClipBoard() {
        val title = characterViewModel.character.value?.name
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", title)
        clipboard.setPrimaryClip(clipData)

        Toast.makeText(context, "Copied to clipboard:\n$title", Toast.LENGTH_SHORT).show()
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

    private fun openUrl(url: String) {

        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    private fun shareUrl(url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        val title = characterViewModel.character.value?.name
        val data = "$title\n\n$url\n\nShared Using Sushi - MAL Client"
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, data)
        startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
    }


}

