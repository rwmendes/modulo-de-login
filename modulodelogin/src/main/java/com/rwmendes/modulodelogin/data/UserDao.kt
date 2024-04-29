package com.rwmendes.modulodelogin.data

import androidx.room.*
import com.rwmendes.modulodelogin.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUser(username: String, password: String): User?

    // Método para limpar/deletar o usuário
    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteUser(username: String)
}
