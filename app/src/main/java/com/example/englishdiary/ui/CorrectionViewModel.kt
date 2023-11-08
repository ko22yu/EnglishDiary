package com.example.englishdiary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishdiary.common.Constants
import com.example.englishdiary.common.NetworkResponse
import com.example.englishdiary.data.remote.Message
import com.example.englishdiary.domain.model.CorrectionResult
import com.example.englishdiary.domain.model.DiaryExample
import com.example.englishdiary.domain.usecase.CorrectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CorrectionViewModel @Inject constructor(
    private val correctionUseCase: CorrectionUseCase
) : ViewModel() {

    private var _diaryExample: MutableStateFlow<DiaryExample> = MutableStateFlow(DiaryExample(""))
    val diaryExample = _diaryExample.asStateFlow()
    private var _correctionResults: MutableStateFlow<List<CorrectionResult>> =
        MutableStateFlow(
            listOf(CorrectionResult(correctedEnText = "", jaText = "", reasonForCorrection = ""))
        )
    val correctionResults = _correctionResults.asStateFlow()
    val inputEnglishText = MutableLiveData<String>()
    private val _isCorrectionButtonEnabled = MutableLiveData<Boolean>()
    val isCorrectionButtonEnabled: LiveData<Boolean> = _isCorrectionButtonEnabled

    private var _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private var _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    init {
        getDiaryExample(
            listOf(Message(role = "user", content = Constants.DEBUG_PROMPT_TO_GET_EXAMPLE_DIARY))
        )
    }

    fun getDiaryExample(messages: List<Message?>?) {
        correctionUseCase.getDiaryExample(messages).onEach {
            when (it) {
                is NetworkResponse.Success -> {
                    _isLoading.value = false
                    _diaryExample.value = it.data!!
                }

                is NetworkResponse.Failure -> {
                    _error.value = true
                }

                is NetworkResponse.Loading -> {
                    _isLoading.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onClickCorrectionButton(messages: List<Message?>?) {
        // カンマで分割
        // 例文と英語を交互にPROMPT_FOR_CORRECTIONに追加
        // APIに送る
        correctionUseCase.getCorrectionResults(messages).onEach { networkState ->
            when (networkState) {
                is NetworkResponse.Success -> {
                    _isLoading.value = false
                    _correctionResults.value = networkState.data ?: listOf()
                }

                is NetworkResponse.Failure -> {
                    _error.value = true
                }

                is NetworkResponse.Loading -> {
                    _isLoading.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateCorrectionButtonEnabled() {
        _isCorrectionButtonEnabled.value = inputEnglishText.value?.isNotEmpty()
    }
}