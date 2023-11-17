package com.example.englishdiary.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.englishdiary.R
import com.example.englishdiary.common.Constants
import com.example.englishdiary.databinding.FragmentSettingDialogBinding
import com.example.englishdiary.ui.CorrectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentSettingDialogBinding
    private val viewModel: SettingViewModel by viewModels()
    private val correctionViewModel: CorrectionViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        // ダイアログの背景を透過にする
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = FragmentSettingDialogBinding.inflate(requireActivity().layoutInflater)

        binding.dairyExampleTextLengthPreferenceRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.short_length_radio_button -> {
                    if (binding.shortLengthRadioButton.isChecked) {
                        viewModel.setDiaryExampleTextLengthPreference(Constants.SHORT_LENGTH)
                    }
                }

                R.id.medium_length_radio_button -> {
                    if (binding.mediumLengthRadioButton.isChecked) {
                        viewModel.setDiaryExampleTextLengthPreference(Constants.MEDIUM_LENGTH)
                    }
                }

                R.id.long_length_radio_button -> {
                    if (binding.longLengthRadioButton.isChecked) {
                        viewModel.setDiaryExampleTextLengthPreference(Constants.LONG_LENGTH)
                    }
                }
            }
        }

        when (Constants.length) {
            Constants.SHORT_LENGTH -> binding.shortLengthRadioButton.isChecked = true
            Constants.MEDIUM_LENGTH -> binding.mediumLengthRadioButton.isChecked = true
            Constants.LONG_LENGTH -> binding.longLengthRadioButton.isChecked = true
        }

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

        viewModel.setBeginningTextLengthPreference()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        if (viewModel.beginningTextLengthPreference.value != viewModel.diaryExampleTextLengthPreference.value) {
            correctionViewModel.onRefresh(isCalledFromPullToRefresh = false)
        }
    }
}