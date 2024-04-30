package com.rwmendes.modulodelogin.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmendes.modulodelogin.R
import kotlinx.coroutines.launch
import com.rwmendes.modulodelogin.data.LoginRepository
import com.rwmendes.modulodelogin.data.Result
import com.rwmendes.modulodelogin.data.CredentialsIncorrectException
import com.rwmendes.modulodelogin.data.UserNotFoundException
import com.rwmendes.modulodelogin.data.NetworkErrorException


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            when (result) {
                is Result.Success -> {
                    _loginResult.postValue(LoginResult.Success(LoggedInUserView(displayName = result.data.username)))
                }
                is Result.Error -> {
                    _loginResult.postValue(LoginResult.Error(mapErrorToResource(result.exception)))
                }
            }
        }
    }

    private fun mapErrorToResource(e: Exception): Int {
        return when (e) {
            // Assumindo que as exceções apropriadas sejam definidas para lidar com cada caso
            is CredentialsIncorrectException -> R.string.invalid_credentials
            is UserNotFoundException -> R.string.user_not_found
            is NetworkErrorException -> R.string.network_error
            else -> R.string.login_failed
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.postValue(LoginFormState(usernameError = R.string.invalid_username))
        } else if (!isPasswordValid(password)) {
            _loginForm.postValue(LoginFormState(passwordError = R.string.invalid_password))
        } else {
            _loginForm.postValue(LoginFormState(isDataValid = true))
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
