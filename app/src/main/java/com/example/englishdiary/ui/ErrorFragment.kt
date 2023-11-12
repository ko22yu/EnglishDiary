package com.example.englishdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.englishdiary.R
import com.example.englishdiary.databinding.FragmentErrorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ErrorFragment : Fragment() {
    private lateinit var binding: FragmentErrorBinding
    private val viewModel: CorrectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentErrorBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reloadButton.setOnClickListener {
            viewModel.onRefresh(currentFragmentIsErrorFragment = true)

            viewModel.navigateToCorrection.observe(viewLifecycleOwner) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CompositionFragment())
                    .commit()
            }
        }

        lifecycleScope.launch {
            viewModel.showNetworkErrorToastInErrorFragment.collect {
                if (it) Toast.makeText(activity, R.string.network_error_message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}