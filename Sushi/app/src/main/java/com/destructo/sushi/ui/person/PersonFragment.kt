package com.destructo.sushi.ui.person

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
import com.destructo.sushi.BASE_MAL_PEOPLE_URL
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentPagerAdapter
import com.destructo.sushi.databinding.FragmentPersonBinding
import com.destructo.sushi.network.Status
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
class PersonFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {

    private val personViewModel: PersonViewModel by viewModels()
    private lateinit var binding:FragmentPersonBinding
    private lateinit var personPager: ViewPager2
    private lateinit var personPagerAdapter: FragmentStateAdapter
    private lateinit var personTabLayout: TabLayout
    private var personArg: Int = 0
    private lateinit var personTabMediator: TabLayoutMediator
    private lateinit var toolbar: Toolbar
    private lateinit var appBar: AppBarLayout
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null){
            personArg = PersonFragmentArgs.fromBundle(requireArguments()).personId
            personViewModel.getPersonData(personArg)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonBinding
            .inflate(inflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        toolbar = binding.toolbar
        personPager = binding.personViewPager
        personTabLayout = binding.personTabLayout
        appBar = binding.appbar
        collapsingToolbarLayout = binding.personCollapsingToolbar
        collapsingToolbarLayout.setOnLongClickListener {
            copyPersonNameToClipBoard()
            return@setOnLongClickListener false
        }

        personTabMediator = TabLayoutMediator(personTabLayout, personPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.about)
                }
                1 -> {
                    tab.text = getString(R.string.voice_acting)
                }
                2 -> {
                    tab.text = getString(R.string.anime_staff_role)
                }
                3 -> {
                    tab.text = getString(R.string.published_manga)
                }
            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()

        personViewModel.personData.observe(viewLifecycleOwner) { resource ->
            when(resource.status){
                Status.LOADING->{

                }
                Status.SUCCESS->{
                    binding.personEntity = resource.data
                    val fragmentList = arrayListOf(
                        PersonAboutFragment(),
                        PersonVoiceActingFragment(),
                        PersonAnimeStaffFragment(),
                        PublishedMangaFragment()
                    )
                    setupViewPager(fragmentList)
                }
                Status.ERROR -> {

                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        appBar.addOnOffsetChangedListener(this)
    }

    private fun setupViewPager(fragmentList:ArrayList<Fragment>){
        personPagerAdapter = FragmentPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )
        personPager.adapter = personPagerAdapter
        personTabMediator.attach()

    }

    fun setupToolbar(){
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        toolbar.setOnLongClickListener {
            copyPersonNameToClipBoard()
            return@setOnLongClickListener false
        }
        toolbar.inflateMenu(R.menu.detail_menu_options)
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.share_item -> sharePerson()
                R.id.copy_title -> copyPersonNameToClipBoard()
                R.id.open_in_browser -> openInBrowser()
            }
            false
        }
    }

    private fun openInBrowser() {
        val url = BASE_MAL_PEOPLE_URL + personArg
        context?.openUrl(url)
    }

    private fun sharePerson() {
        personViewModel.personData.value?.data?.name?.let {
            val url = BASE_MAL_PEOPLE_URL + personArg
            val data = String.format(getString(R.string.share_anime_or_manga), it, url)
            context?.shareText(data)
        }
    }

    private fun copyPersonNameToClipBoard() {
        personViewModel.personData.value?.data?.name?.let{
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