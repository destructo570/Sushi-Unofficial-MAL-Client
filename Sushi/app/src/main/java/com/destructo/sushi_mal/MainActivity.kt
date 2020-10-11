package com.destructo.sushi_mal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anime.*
import kotlinx.android.synthetic.main.layout_drawer.*

class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragemnt)
    }
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

    private lateinit var navListener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setupDrawerLayout()
    }

    private fun setupDrawerLayout(){
        navigationView.setupWithNavController(navController)
        toolbar.setupWithNavController( navController, appBarConfig)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

}