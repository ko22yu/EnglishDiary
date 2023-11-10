package com.example.englishdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.englishdiary.databinding.FragmentErrorBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ErrorFragment : Fragment() {
    private lateinit var binding: FragmentErrorBinding
    private val viewModel: CorrectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentErrorBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }
}