package com.kafasan.store.ui.pages.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafasan.store.domain.local.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val prefs: AppPreferences,
    ) : ViewModel() {
        private val _state = MutableStateFlow(State())
        val state: StateFlow<State> = _state.asStateFlow()

        fun simulateLogin(
            isRemember: Boolean,
            email: String,
            password: String,
        ) {
            val emailValidation = email.split("@")
            if (emailValidation.size != 2) {
                _state.value = _state.value.copy(error = FieldError.EMAIL)
                return
            }
            if (password.isEmpty()) {
                _state.value = _state.value.copy(error = FieldError.PASSWORD)
                return
            }

            viewModelScope.launch {
                prefs.toggleRememberMe(isRemember)
                _state.value = _state.value.copy(loading = true)
                delay(2000)
                _state.value = State(loading = false, isSuccess = true)
            }
        }

        data class State(
            val loading: Boolean = false,
            val error: FieldError = FieldError.NO_ERROR,
            val isSuccess: Boolean = false,
        )

        enum class FieldError(val message: String) {
            EMAIL("invalid email"),
            PASSWORD("password must not empty"),
            NO_ERROR(""),
        }
    }
