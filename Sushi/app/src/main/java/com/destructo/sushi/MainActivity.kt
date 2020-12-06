package com.destructo.sushi

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.destructo.sushi.enum.AppTheme
import com.destructo.sushi.util.SessionManager
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.profile_header_layout.view.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment:NavHostFragment
    private lateinit var navController:NavController

    private lateinit var navView:NavigationView

    private lateinit var profileHeader: ConstraintLayout
    private val mainViewModel: MainViewModel by viewModels()


    @Inject
    lateinit var sessionManager: SessionManager

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
                //R.id.clubFragment,
                //R.id.forumFragment,
                //R.id.newsFragment,
                //R.id.promotionFragment,
                R.id.settingsFragment,
            ), drawer_layout
        )
    }

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val currentTheme = sharedPref.getString(CURRENT_THEME, AppTheme.LIGHT.value)

        setApplicationTheme(currentTheme)

        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragemnt) as NavHostFragment
        navController = navHostFragment.navController

        navView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawer_layout)
        profileHeader = navView.getHeaderView(0) as ConstraintLayout
        setupDrawerLayout()
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
                R.id.mangaFragment -> {
                    navView.setCheckedItem(R.id.mangaFragment)
                }
                R.id.settingsFragment -> {
                    navView.setCheckedItem(R.id.settingsFragment)
                }
            }
        }

        profileHeader.setOnClickListener {
            navController.navigate(R.id.profileFragment)
            drawer_layout.closeDrawer(GravityCompat.START)

        }

        mainViewModel.userInfoEntity.observe(this){
            it?.userInfo?.let {userInfo->
                profileHeader.header_user_name.text = userInfo.name
                profileHeader.header_user_location.text = userInfo.location

                Glide.with(profileHeader.header_user_image.context)
                    .load(userInfo.picture)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.test_img)
                    )
                    .into(profileHeader.header_user_image)
            }
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
            AppTheme.NIGHT.value ->{setTheme(R.style.AppTheme_Dark)}
            AppTheme.AMOLED.value ->{setTheme(R.style.AppTheme_Amoled)}
            AppTheme.AMOLED_LIME.value ->{setTheme(R.style.AppTheme_Amoled_Lime)}
            AppTheme.AMOLED_SAKURA.value ->{setTheme(R.style.AppTheme_Amoled_Sakura)}
            AppTheme.AMOLED_CYAN.value ->{setTheme(R.style.AppTheme_Amoled_Cyan)}

            else ->{setTheme(R.style.AppTheme)}
        }
    }


}