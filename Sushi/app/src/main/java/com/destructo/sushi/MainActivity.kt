package com.destructo.sushi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragemnt)
    }
    private lateinit var navView2:NavigationView
    private lateinit var navView:NavigationView

     private val appBarConfig by lazy {
        AppBarConfiguration(
            setOf(
                R.id.myAnimeListFragment,
                R.id.myMangaListFragment,
                R.id.animeFragment,
                R.id.scheduleFragment,
                R.id.mangaFragment,
                //R.id.clubFragment,
                //R.id.forumFragment,
                //R.id.newsFragment,
                //R.id.promotionFragment,
                R.id.settingsFragment,
                R.id.aboutFragment
            ), drawer_layout)
    }

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.navigationView)
        navView2 = findViewById(R.id.navigationView2)
        drawerLayout = findViewById(R.id.drawer_layout)
        setupDrawerLayout()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.myAnimeListFragment->{
                    navView.setCheckedItem(R.id.animeFragment)
                }
                R.id.myMangaListFragment->{
                    navView.setCheckedItem(R.id.myMangaListFragment)
                }
                R.id.animeFragment->{
                    navView.setCheckedItem(R.id.animeFragment)
                }
                R.id.scheduleFragment->{
                    navView.setCheckedItem(R.id.scheduleFragment)
                }
                R.id.mangaFragment->
                    navView.setCheckedItem(R.id.mangaFragment)

                R.id.settingsFragment->{
                    navView.setCheckedItem(R.id.settingsFragment)
                }
                R.id.aboutFragment->{        
                    navView.setCheckedItem(R.id.aboutFragment)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,appBarConfig)
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

}