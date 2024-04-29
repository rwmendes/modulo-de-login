package com.rwmendes.modulodelogin.data

import com.rwmendes.modulodelogin.data.model.LoggedInUser


class LoginRepository(val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    suspend fun logout() {
        user = null
        dataSource.logout()  // Chama a função suspend do dataSource
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(username, password)
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}
