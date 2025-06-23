package com.kafasan.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafasan.store.domain.local.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefs: AppPreferences
): ViewModel() {
    private val _authState = MutableStateFlow(AuthState.CHECKING)
    val authState = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.isRememberMe.collectLatest {
                _authState.value = if (it) AuthState.AUTHORIZE else AuthState.UN_AUTHORIZE
            }
        }
    }

    enum class AuthState {
        CHECKING, UN_AUTHORIZE, AUTHORIZE,
    }
}