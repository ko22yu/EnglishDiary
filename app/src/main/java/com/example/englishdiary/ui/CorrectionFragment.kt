package com.example.englishdiary.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.englishdiary.R
import com.example.englishdiary.databinding.FragmentCorrectionBinding
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

        viewModel.navigateToError.observe(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment())
                .commit()
        }

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
            viewModel.isLoading.collect {
                viewModel.updateCorrectionButtonEnabled()
            }
        }
    }

}