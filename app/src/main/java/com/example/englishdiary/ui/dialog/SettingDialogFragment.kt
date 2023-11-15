package com.example.englishdiary.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.englishdiary.R
import com.example.englishdiary.databinding.FragmentSettingDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentSettingDialogBinding
    private val viewModel: SettingViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        // ダイアログの背景を透過にする
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = FragmentSettingDialogBinding.inflate(requireActivity().layoutInflater)

        binding.darkModePreferenceRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.follow_system_radio_button -> {
                    if (binding.followSystemRadioButton.isChecked) {
                        viewModel.setNightModePreference(MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }

                R.id.light_mode_radio_button -> {
                    if (binding.lightModeRadioButton.isChecked) {
                        viewModel.setNightModePreference(MODE_NIGHT_NO)
                    }
                }

                R.id.dark_mode_radio_button -> {
                    if (binding.darkModeRadioButton.isChecked) {
                        viewModel.setNightModePreference(MODE_NIGHT_YES)
                    }
                }
            }
            viewModel.readNightModePreference()
        }

        lifecycleScope.launch {
            viewModel.nightModePreference.collect { mode ->
                when (mode) {
                    MODE_NIGHT_FOLLOW_SYSTEM -> binding.followSystemRadioButton.isChecked = true
                    MODE_NIGHT_NO -> binding.lightModeRadioButton.isChecked = true
                    MODE_NIGHT_YES -> binding.darkModeRadioButton.isChecked = true
                }
            }
        }

        dialog.setContentView(binding.root)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(null)
        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.7).toInt()
        dialog?.window?.setLayout(width, height)
    }

}