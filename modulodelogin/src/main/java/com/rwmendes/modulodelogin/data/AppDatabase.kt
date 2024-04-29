package com.rwmendes.modulodelogin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rwmendes.modulodelogin.data.model.User

// Anotação Database para definir as entidades e a versão do banco de dados
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // Singleton previne múltiplas instâncias do banco de dados abrindo ao mesmo tempo.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Função para obter a instância do banco de dados
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
