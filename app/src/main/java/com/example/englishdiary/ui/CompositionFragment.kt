package com.example.englishdiary.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.englishdiary.R
import com.example.englishdiary.databinding.FragmentCompositionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompositionFragment : Fragment() {
    private lateinit var binding: FragmentCompositionBinding
    private val viewModel: CorrectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompositionBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = view.findNavController()

        if (viewModel.diaryExample.value.content == "") viewModel.onCreate()

        viewModel.navigateToError.observe(viewLifecycleOwner) {
            navController.navigate(R.id.action_compositionFragment_to_errorFragment)
        }

        viewModel.navigateToCorrectionResult.observe(viewLifecycleOwner) {
            navController.navigate(R.id.action_compositionFragment_to_correctionResultFragment)
        }

        binding.correctionButton.setOnClickListener {
            viewModel.onClickCorrectionButton()
        }

        // EditTextの入力を監視して、文字が入力されたら添削ボタンのenableを更新する
        binding.englishCompositionInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateCorrectionButtonEnabled()
            }
        })

        // ローディング状態が変化したときにも添削ボタンのenableを更新する
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                viewModel.updateCorrectionEditTextEnabled()
                viewModel.updateCorrectionButtonEnabled()
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            lifecycleScope.launch {
                viewModel.isRefreshing.collect {
                    if (!it) binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

}