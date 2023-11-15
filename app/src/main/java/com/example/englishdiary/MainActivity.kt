package com.example.englishdiary

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.englishdiary.ui.dialog.SettingDialogFragment
import com.example.englishdiary.ui.dialog.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val viewModel: SettingViewModel by viewModels()

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
                if (destination.id == R.id.errorFragment) View.GONE else View.VISIBLE
//            supportActionBar?.title =
//                if (destination.id == R.id.correctionResultFragment) "添削結果" else ""
        }
        // 特定のフラグメントだけUpアイコンを表示させない
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.errorFragment, R.id.compositionFragment))

        setupActionBarWithNavController(navController, appBarConfiguration)

        lifecycleScope.launch {
            viewModel.nightModePreference.collect { mode ->
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_button -> {
                SettingDialogFragment().show(
                    supportFragmentManager,
                    SettingDialogFragment::class.simpleName
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}