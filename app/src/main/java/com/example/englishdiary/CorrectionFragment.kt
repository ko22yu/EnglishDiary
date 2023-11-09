package com.example.englishdiary

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.englishdiary.databinding.FragmentCorrectionBinding
import com.example.englishdiary.ui.CorrectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CorrectionFragment : Fragment() {
    private lateinit var binding: FragmentCorrectionBinding
    private val viewModel: CorrectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCorrectionBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.correctionButton.setOnClickListener {
            viewModel.onClickCorrectionButton()
        }

        // EditTextの入力を監視して、文字が入力されたら添削ボタンのenableを更新する
        binding.englishCompositionInputField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateCorrectionButtonEnabled()
            }
        })

        // ローディング状態が変化したときにも添削ボタンのenableを更新する
        lifecycleScope.launch {
            lifecycleScope.launch {
                viewModel.isLoading.collect {
                    viewModel.updateCorrectionButtonEnabled()
                }
            }
        }

        lifecycleScope.launch {
            lifecycleScope.launch {
                viewModel.error.collect {
                    if (viewModel.error.value)
                        viewModel.showNetworkErrorToast(context = activity)
                }
            }
        }
    }

}