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

    private val appBarConfig by lazy {
        AppBarConfiguration(
            setOf(R.id.animeFragment,
                R.id.scheduleFragment,
                R.id.mangaFragment,
                R.id.clubFragment,
                R.id.forumFragment,
                R.id.newsFragment,
                R.id.promotionFragment,
                R.id.settingsFragment,
                R.id.aboutFragment
            ), drawer_layout)
    }

    private lateinit var toolbar:Toolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView2 = findViewById(R.id.navigationView2)
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Browse Anime"
        setupDrawerLayout()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.animeDetailFragment->{
                    toolbar.visibility = View.GONE
                }
            }
        }

    }

    private fun setupDrawerLayout(){
        navigationView.setupWithNavController(navController)
        toolbar.setupWithNavController( navController, appBarConfig)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            toolbar.visibility = View.VISIBLE
            super.onBackPressed()
        }
    }

}