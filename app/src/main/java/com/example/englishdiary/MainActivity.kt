package com.example.englishdiary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 特定のフラグメントだけToolbarを表示させない
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.visibility =
                if ((destination.id == R.id.errorFragment) or (destination.id == R.id.compositionFragment)) View.GONE else View.VISIBLE
            supportActionBar?.title =
                if (destination.id == R.id.correctionResultFragment) "添削結果" else ""
        }
        // 特定のフラグメントだけUpアイコンを表示させない
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.errorFragment, R.id.compositionFragment))

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}