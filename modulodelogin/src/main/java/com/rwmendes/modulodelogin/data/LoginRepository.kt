package com.rwmendes.modulodelogin.data

import com.rwmendes.modulodelogin.data.model.LoggedInUser
import com.rwmendes.modulodelogin.data.model.User
import java.io.IOException

class LoginRepository(private val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    // Logout clear the user
    suspend fun logout() {
        user = null
        dataSource.logout()  // Assume this method exists in dataSource and clears user data
    }

    // Login or register if user does not exist
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val userExists = dataSource.checkUserExists(username)

        if (userExists) {
            val result = dataSource.login(username, password)
            if (result is Result.Success) {
                setLoggedInUser(result.data)
                return result
            } else {
                return Result.Error(IOException("Senha incorreta"))
            }
        } else {
            // Registra o usuário se não existir
            return register(username, password)
        }
    }

    // Helper function to set the logged in user
    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

    // Register a new user
    private suspend fun register(username: String, password: String): Result<LoggedInUser> {
        return dataSource.register(username, password).also { result ->
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }
        }
    }

}
