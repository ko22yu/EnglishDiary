package com.example.englishdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.englishdiary.common.Constants
import com.example.englishdiary.data.remote.Message
import com.example.englishdiary.data.remote.OpenAiRequestDto
import com.example.englishdiary.databinding.ActivityMainBinding
import com.example.englishdiary.ui.CorrectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: CorrectionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.correctionButton.setOnClickListener {
            viewModel.onClickCorrectionButton(
                messages = listOf(
                    Message(role = "user", content = Constants.DEBUG_PROMPT_FOR_CORRECTION)
                ),
            )
        }
    }
}