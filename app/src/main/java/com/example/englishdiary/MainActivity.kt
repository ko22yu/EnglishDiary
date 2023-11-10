package com.example.englishdiary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.englishdiary.ui.CorrectionFragment
import com.example.englishdiary.ui.CorrectionViewModel
import com.example.englishdiary.ui.ErrorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val correctionViewModel: CorrectionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CorrectionFragment())
            .commit()

        correctionViewModel.navigateToError.observe(this) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment())
                .commit()
        }
    }
}