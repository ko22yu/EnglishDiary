package com.example.englishdiary.ui.dialog

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishdiary.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val NIGHT_MODE_PREFERENCE = intPreferencesKey("nightModePreference")

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {
    private val context: Context = application.applicationContext

    private val _diaryExampleTextLengthPreference = MutableStateFlow(Constants.MEDIUM_LENGTH)
    val diaryExampleTextLengthPreference: StateFlow<Int> get() = _diaryExampleTextLengthPreference
    private val _nightModePreference = MutableStateFlow(Int.MIN_VALUE)
    val nightModePreference: StateFlow<Int> get() = _nightModePreference

    init {
        readNightModePreference()
    }

    fun readNightModePreference() {
        viewModelScope.launch {
            context.dataStore.data
                .map { preferences ->
                    val mode = preferences[NIGHT_MODE_PREFERENCE] ?: MODE_NIGHT_FOLLOW_SYSTEM
                    if (mode != Int.MIN_VALUE) mode else MODE_NIGHT_FOLLOW_SYSTEM
                }
                .collect { value ->
                    _nightModePreference.value = value
                }
        }
    }

    fun setNightModePreference(mode: Int) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[NIGHT_MODE_PREFERENCE] = mode
            }
        }
    }

    fun setDiaryExampleTextLengthPreference(length: Int) {
        Constants.length = length
        _diaryExampleTextLengthPreference.value = length
    }
}