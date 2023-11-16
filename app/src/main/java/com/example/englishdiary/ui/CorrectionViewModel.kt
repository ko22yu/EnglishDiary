package com.example.englishdiary.ui

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishdiary.common.Constants
import com.example.englishdiary.common.NetworkResponse
import com.example.englishdiary.common.SingleLiveEvent
import com.example.englishdiary.data.remote.Message
import com.example.englishdiary.domain.model.CorrectionResult
import com.example.englishdiary.domain.model.DiaryExample
import com.example.englishdiary.domain.usecase.CorrectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CorrectionViewModel @Inject constructor(
    private val correctionUseCase: CorrectionUseCase
) : ViewModel() {

    private val _navigateToError: SingleLiveEvent<Unit> = SingleLiveEvent()
    val navigateToError: LiveData<Unit> = _navigateToError
    private val _navigateToComposition: SingleLiveEvent<Unit> = SingleLiveEvent()
    val navigateToComposition: LiveData<Unit> = _navigateToComposition
    private val _navigateToCorrectionResult: SingleLiveEvent<Unit> = SingleLiveEvent()
    val navigateToCorrectionResult: LiveData<Unit> = _navigateToCorrectionResult

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
    private val _isCorrectionEditTextEnabled = MutableLiveData<Boolean>()
    val isCorrectionEditTextEnabled: LiveData<Boolean> = _isCorrectionEditTextEnabled

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    private var _isBeingCorrected = MutableStateFlow(false)
    val isBeingCorrected = _isBeingCorrected.asStateFlow()
    private var _showNetworkErrorToastInErrorFragment = MutableStateFlow(false)
    val showNetworkErrorToastInErrorFragment = _showNetworkErrorToastInErrorFragment.asStateFlow()

    private var _progressBarVisibility: MutableStateFlow<Int> =
        MutableStateFlow(ProgressBar.VISIBLE)
    val progressBarVisibility = _progressBarVisibility.asStateFlow()
    private var _placeHolderForDiaryExampleTextVisibility: MutableStateFlow<Int> =
        MutableStateFlow(View.VISIBLE)
    val placeHolderForDiaryExampleTextVisibility =
        _placeHolderForDiaryExampleTextVisibility.asStateFlow()

    init {
        watchProgressBarVisibility()
        watchPlaceHolderVisibility()
    }

    fun onCreate() {
        getDiaryExample(
            listOf(Message(role = "user", content = Constants.DEBUG_PROMPT_TO_GET_EXAMPLE_DIARY))
        )
    }

    fun onRefresh(
        currentFragmentIsErrorFragment: Boolean = false,
        isCalledFromOnRefresh: Boolean = true,
    ) {
        getDiaryExample(
            listOf(
                Message(
                    role = "user",
                    content = Constants.DEBUG_PROMPT_TO_GET_EXAMPLE_DIARY
                )
            ),
            currentFragmentIsErrorFragment = currentFragmentIsErrorFragment,
            isCalledFromOnRefresh = isCalledFromOnRefresh,
        )
    }

    fun getDiaryExample(
        messages: List<Message?>?,
        currentFragmentIsErrorFragment: Boolean = false,
        isCalledFromOnRefresh: Boolean = false,
    ) {
        correctionUseCase.getDiaryExample(messages).onEach {
            when (it) {
                is NetworkResponse.Success -> {
                    _isLoading.value = false
                    if (isCalledFromOnRefresh and !currentFragmentIsErrorFragment) {
                        _isRefreshing.value = false
                    }
                    if (currentFragmentIsErrorFragment) _navigateToComposition.value = Unit
                    _diaryExample.value = it.data ?: DiaryExample("")
                }

                is NetworkResponse.Failure -> {
                    _isLoading.value = false
                    if (!currentFragmentIsErrorFragment) _navigateToError.value = Unit
                    else _showNetworkErrorToastInErrorFragment.value = true
                    Log.d("log-networkResponseFailure", it.error.toString())
                }

                is NetworkResponse.Loading -> {
                    _isLoading.value = true
                    if (isCalledFromOnRefresh and !currentFragmentIsErrorFragment) {
                        _isRefreshing.value = true
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onClickCorrectionButton() {
        addDiaryExampleAndEnglishCompositionToPromptForCorrection()
        val messages: List<Message?> =
            listOf(Message(role = "user", content = Constants.PROMPT_FOR_CORRECTION))

        correctionUseCase.getCorrectionResults(messages).onEach { networkState ->
            when (networkState) {
                is NetworkResponse.Success -> {
                    _isLoading.value = false
                    _isBeingCorrected.value = false
                    _navigateToCorrectionResult.value = Unit
                    _correctionResults.value = networkState.data ?: listOf()
                }

                is NetworkResponse.Failure -> {
                    _isLoading.value = false
                    _isBeingCorrected.value = false
                    _navigateToError.value = Unit
                }

                is NetworkResponse.Loading -> {
                    _isLoading.value = true
                    _isBeingCorrected.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun addDiaryExampleAndEnglishCompositionToPromptForCorrection() {
        val inputEnglishSentenceList = inputEnglishText.value?.split(".")
        val diaryExampleSentenceList = diaryExample.value.content.split("。")
        val diaryExampleAndEnglishCompositionList = mutableListOf<String>()
        val maxSize = maxOf(inputEnglishSentenceList?.size ?: 0, diaryExampleSentenceList.size)
        for (i in 0 until maxSize) {
            if (i < diaryExampleSentenceList.size) {
                diaryExampleAndEnglishCompositionList.add(diaryExampleSentenceList[i])
            }
            if (inputEnglishSentenceList != null && i < inputEnglishSentenceList.size) {
                diaryExampleAndEnglishCompositionList.add(inputEnglishSentenceList[i])
            }
        }

        diaryExampleAndEnglishCompositionList.forEach {
            Constants.PROMPT_FOR_CORRECTION += it + "\n"
        }
    }

    fun updateCorrectionButtonEnabled() {
        _isCorrectionButtonEnabled.value =
            inputEnglishText.value?.isNotEmpty() == true && !isLoading.value && !isRefreshing.value
    }

    fun updateCorrectionEditTextEnabled() {
        _isCorrectionEditTextEnabled.value = !isLoading.value && !isRefreshing.value
    }

    private fun watchProgressBarVisibility() {
        viewModelScope.launch {
            isLoading.collect {
                if (it and !isRefreshing.value) _progressBarVisibility.value = ProgressBar.VISIBLE
                else _progressBarVisibility.value = ProgressBar.INVISIBLE
            }
        }
    }

    private fun watchPlaceHolderVisibility() {
        viewModelScope.launch {
            isLoading.collect {
                if (it and !isRefreshing.value and !isBeingCorrected.value)
                    _placeHolderForDiaryExampleTextVisibility.value = View.VISIBLE
                else _placeHolderForDiaryExampleTextVisibility.value = View.INVISIBLE
            }
        }
    }
}