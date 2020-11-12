package com.destructo.sushi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.destructo.sushi.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.profile_header_layout.view.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment:NavHostFragment
    private lateinit var navController:NavController

    private lateinit var navView2:NavigationView
    private lateinit var navView:NavigationView

    private lateinit var profileHeader: View
    private lateinit var binding: ActivityMainBinding

     private val appBarConfig by lazy {
        AppBarConfiguration(
            setOf(
                R.id.profileFragment,
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

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragemnt) as NavHostFragment
        navController = navHostFragment.navController


        navView = findViewById(R.id.navigationView)
        navView2 = findViewById(R.id.navigationView2)
        drawerLayout = findViewById(R.id.drawer_layout)
        profileHeader = navView.getHeaderView(0)
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
                R.id.mangaFragment->{
                    navView.setCheckedItem(R.id.mangaFragment)
                }
                R.id.settingsFragment->{
                    navView.setCheckedItem(R.id.settingsFragment)
                }
                R.id.aboutFragment->{        
                    navView.setCheckedItem(R.id.aboutFragment)
                }
            }
        }

        profileHeader.setOnClickListener {
            navController.navigate(R.id.profileFragment)
            drawer_layout.closeDrawer(GravityCompat.START)
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