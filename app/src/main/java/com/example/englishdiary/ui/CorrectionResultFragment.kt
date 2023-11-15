package com.example.englishdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishdiary.databinding.FragmentCorrectionResultBinding
import com.example.englishdiary.ui.dialog.adapter.CorrectionResultItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CorrectionResultFragment : Fragment() {
    private lateinit var binding: FragmentCorrectionResultBinding
    private val viewModel: CorrectionViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

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

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val correctionResultAdapter = CorrectionResultItemAdapter()
        recyclerView.adapter = correctionResultAdapter

        lifecycleScope.launch {
            viewModel.correctionResults.collect { items ->
                correctionResultAdapter.submitList(items)
            }
        }
    }
}