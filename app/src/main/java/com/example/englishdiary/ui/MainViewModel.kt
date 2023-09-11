package com.example.englishdiary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishdiary.common.NetworkResponse
import com.example.englishdiary.data.remote.Message
import com.example.englishdiary.data.remote.OpenAiRequestDto
import com.example.englishdiary.domain.model.DiaryExample
import com.example.englishdiary.domain.usecase.GetDiaryExampleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDiaryExampleUseCase: GetDiaryExampleUseCase
): ViewModel() {

    private var _diaryExample: MutableStateFlow<DiaryExample> = MutableStateFlow(DiaryExample(""))
    val diaryExample = _diaryExample.asStateFlow()

    private var _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private var _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    init {
        getDiaryExample(
            OpenAiRequestDto(
                model = "gpt-3.5-turbo",
                messages = listOf( Message(role = "user", content = "Say this is a test!") ),
                temperature = 0.7
            )
        )
    }

    fun getDiaryExample(body: OpenAiRequestDto) {
        getDiaryExampleUseCase(body).onEach {
            when(it) {
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
}