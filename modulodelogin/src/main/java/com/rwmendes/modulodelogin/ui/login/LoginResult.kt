package com.rwmendes.modulodelogin.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
sealed class LoginResult {
     data class Success(val userView: LoggedInUserView): LoginResult()
     data class Error(val errorType: Int): LoginResult()
}