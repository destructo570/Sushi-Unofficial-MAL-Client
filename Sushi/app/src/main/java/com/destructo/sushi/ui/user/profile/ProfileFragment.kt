package com.destructo.sushi.ui.user.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.destructo.sushi.R
import com.destructo.sushi.adapter.pagerAdapter.FragmentPagerAdapter
import com.destructo.sushi.databinding.FragmentProfileBinding
import com.destructo.sushi.network.Status
import com.destructo.sushi.ui.auth.LoginActivity
import com.destructo.sushi.util.SessionManager
import com.destructo.sushi.util.getColorFromAttr
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressBar:ProgressBar

    private lateinit var navView: NavigationView
    private lateinit var profileHeader: ConstraintLayout

    private lateinit var pager: ViewPager2
    private lateinit var pagerAdapter: FragmentStateAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var tabMediator: TabLayoutMediator

    @Inject
    lateinit var sessionManager: SessionManager

    private val profileViewModel:ProfileViewModel by viewModels()
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            profileViewModel.getUserInfo(args.username)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        toolbar = binding.toolbar
        progressBar = binding.progressBar
        pager = binding.profileViewPager
        tabLayout = binding.profileTabLayout

        tabMediator = TabLayoutMediator(tabLayout, pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.stats)
                }
                1 -> {
                    tab.text = getString(R.string.favorites)
                }
                2 -> {
                    tab.text = getString(R.string.friend_list)
                }
                3 -> {
                    tab.text = getString(R.string.anime_list)
                }
                4 -> {
                    tab.text = getString(R.string.manga_list)
                }
            }
        }


        navView = requireActivity().navigationView
        profileHeader = navView.getHeaderView(0) as ConstraintLayout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()

        profileViewModel.userInformation.observe(viewLifecycleOwner){resource->
            when(resource.status){
                Status.LOADING->{
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    progressBar.visibility = View.GONE

                    resource.data?.let {userInfo->
                        binding.userInfo = userInfo

                        val fragmentList = arrayListOf(
                            ProfileStatsFragment(),
                            ProfileFavoriteFragment(),
                            ProfileFriendsFragment.newInstance(args.username),
                            ProfileAnimeListFragment.newInstance(args.username),
                            ProfileMangaListFragment.newInstance(args.username)
                            )
                        setupViewPager(fragmentList)
                    }
                }
                Status.ERROR->{
                    Timber.e("Error: %s", resource.message)
                }
            }
        }


    }

    private fun setupViewPager(fragmentList:ArrayList<Fragment>){
        pagerAdapter = FragmentPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )
        pager.adapter = pagerAdapter
        tabMediator.attach()
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.profile)
        toolbar.setNavigationOnClickListener {
            activity?.drawer_layout?.openDrawer(GravityCompat.START)
        }
        toolbar.inflateMenu(R.menu.user_profile_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.user_logout -> {
                    logout()
                }
            }

            false
        }

    }

    private fun logOutOfApp() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun logout(){
        context?.let {context->
            val dialog = AlertDialog.Builder(context, R.style.SushiAlertDialog)
                .setTitle(getString(R.string.confirm_logout))
                .setMessage(getString(R.string.confirm_logout_message))
                .setPositiveButton(R.string.yes
                ) { _, _ ->
                    sessionManager.clearSession()
                    logOutOfApp()
                }
                .setNegativeButton(R.string.no
                ) { _, _ ->

                }
                .create()

            dialog.setOnShowListener {

                val view = dialog.window
                view?.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.drawable_alert_dialog_bg))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColorFromAttr(R.attr.textColorPrimary))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColorFromAttr(R.attr.textColorSecondary))
            }
            dialog.show()
        }


    }
}