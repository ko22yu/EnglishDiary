package com.example.englishdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.englishdiary.databinding.FragmentCorrectionResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CorrectionResultFragment : Fragment() {
    private lateinit var binding: FragmentCorrectionResultBinding
    private val viewModel: CorrectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCorrectionResultBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}