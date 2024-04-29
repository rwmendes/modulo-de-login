package com.rwmendes.modulodelogin.data

import android.content.Context
import com.rwmendes.modulodelogin.data.model.LoggedInUser
import com.rwmendes.modulodelogin.data.model.User
import java.io.IOException

class LoginDataSource(context: Context) {

    private val userDao = AppDatabase.getDatabase(context).userDao()
    private var currentUser: LoggedInUser? = null

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val user = userDao.getUser(username, password)
            if (user != null) {
                currentUser = LoggedInUser(username = username)
                return Result.Success(currentUser!!)
            } else {
                throw IOException("Credenciais inválidas")
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Erro ao tentar logar", e))
        }
    }

    suspend fun register(username: String, password: String): Result<LoggedInUser> {
        try {
            val newUser = User(username, password)
            userDao.insertUser(newUser)
            currentUser = LoggedInUser(username = username)
            return Result.Success(currentUser!!)
        } catch (e: Throwable) {
            return Result.Error(IOException("Erro ao registrar usuário", e))
        }
    }

    suspend fun logout() {
        currentUser?.let {
            userDao.deleteUser(it.username)
        }
        currentUser = null
    }
}
