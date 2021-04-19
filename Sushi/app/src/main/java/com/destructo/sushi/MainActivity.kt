package com.destructo.sushi

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.destructo.sushi.enum.AppTheme
import com.destructo.sushi.room.UserInfoDao
import com.destructo.sushi.util.SessionManager
import com.destructo.sushi.util.show
import com.google.android.gms.ads.*
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.profile_header_layout.view.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment:NavHostFragment
    private lateinit var navController:NavController

    private lateinit var navView:NavigationView
    private lateinit var googleAdView : AdView

    private lateinit var profileHeader: ConstraintLayout
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var fragmentContainerView: FragmentContainerView

    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var userInfoDao: UserInfoDao
    @Inject
    lateinit var sharedPref: SharedPreferences

     private val appBarConfig by lazy {
        AppBarConfiguration(
            setOf(
                R.id.profileFragment,
                R.id.myAnimeListFragment,
                R.id.myMangaListFragment,
                R.id.animeFragment,
                R.id.scheduleFragment,
                R.id.mangaFragment,
                R.id.searchFragment,
//                R.id.forumFragment,
                R.id.settingsFragment,
            ), drawer_layout
        )
    }

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentTheme = sharedPref.getString(CURRENT_THEME, AppTheme.LIGHT.value)

        setApplicationTheme(currentTheme)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragemnt) as NavHostFragment
        navController = navHostFragment.navController
        navView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawer_layout)
        fragmentContainerView = findViewById(R.id.nav_host_fragemnt)
        profileHeader = navView.getHeaderView(0) as ConstraintLayout

        setupDrawerLayout()
        initializeGoogleAdmob()

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)
        mainViewModel.getUserInfo("anime_statistics")
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.profileFragment -> {
                }
                R.id.myAnimeListFragment -> {
                    navView.setCheckedItem(R.id.myAnimeListFragment)
                }
                R.id.myMangaListFragment -> {
                    navView.setCheckedItem(R.id.myMangaListFragment)
                }
                R.id.animeFragment -> {
                    navView.setCheckedItem(R.id.animeFragment)
                }
                R.id.scheduleFragment -> {
                    navView.setCheckedItem(R.id.scheduleFragment)
                }
                R.id.searchFragment -> {
                    navView.setCheckedItem(R.id.searchFragment)
                }
                R.id.forumFragment -> {
                    navView.setCheckedItem(R.id.forumFragment)
                }
                //R.id.mangaFragment -> {
                //    navView.setCheckedItem(R.id.mangaFragment)
                //}
                R.id.settingsFragment -> {
                    navView.setCheckedItem(R.id.settingsFragment)
                }
            }
        }

        mainViewModel.userInfoEntity.observe(this){
            it?.userInfo?.let {userInfo->
                profileHeader.header_user_name.text = userInfo.name
                profileHeader.header_user_location.text = userInfo.location
                setProfileHeaderListener(userInfo.name)

                profileHeader.header_user_image.load(userInfo.picture){
                    placeholder(R.drawable.test_img)
                    crossfade(true)
                    crossfade(200)
                }
            }
        }
    }

    override fun onDestroy() {
        if(::googleAdView.isInitialized && googleAdView != null){
            googleAdView.destroy()
        }
        super.onDestroy()
    }

    private fun initializeGoogleAdmob(){
        if(!BuildConfig.DEBUG){
            if (!SushiApplication.getContext().queryPurchases()) {
                MobileAds.initialize(this) {}

                googleAdView = findViewById(R.id.adView)
                val adRequest = AdRequest.Builder().build()
                googleAdView.loadAd(adRequest)
                googleAdView.adListener = object : AdListener(){

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        Timber.e("Failed To Load Ad")
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        fragmentContainerView.setPadding(0,0,0,140)
                        googleAdView.show()
                        Timber.e("Ad Loaded Successfully")
                    }
                }
            }
        }
    }

    private fun setProfileHeaderListener(username: String?){
        profileHeader.setOnClickListener {

            if (navController.currentDestination?.id != R.id.profileFragment){
                username?.let {
                    navController.navigate(R.id.profileFragment, bundleOf(Pair(USERNAME_ARG, it)))
                }
                drawer_layout.closeDrawer(GravityCompat.START)
            }else{ drawer_layout.closeDrawer(GravityCompat.START) }

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfig)
    }

    private fun setupDrawerLayout(){
        navigationView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    private fun setApplicationTheme(theme: String?){
        when(theme){
            AppTheme.LIGHT.value ->{setTheme(R.style.AppTheme)}
            AppTheme.DARK.value ->{setTheme(R.style.AppTheme_Dark)}
            AppTheme.NIGHT.value ->{setTheme(R.style.AppTheme_Night)}
            AppTheme.AMOLED.value ->{setTheme(R.style.AppTheme_Amoled)}
            else ->{setTheme(R.style.AppTheme)}
        }
    }

}