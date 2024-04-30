package com.rwmendes.modulodelogin.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rwmendes.modulodelogin.data.LoginDataSource
import com.rwmendes.modulodelogin.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */


class LoginViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val loginDataSource = LoginDataSource(applicationContext)
            val loginRepository = LoginRepository(loginDataSource)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
