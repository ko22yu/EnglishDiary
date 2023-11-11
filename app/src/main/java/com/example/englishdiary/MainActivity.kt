package com.example.englishdiary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.englishdiary.ui.CorrectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CorrectionFragment())
            .commit()
    }
}