package com.register.DB

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.register.Model.User

@Database(entities = [User::class], version = 7, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        private val LOCK = Any()
    }

    operator fun invoke(context: Context): UserDatabase {
        if (instance != null) {
            return instance!!
        }
        synchronized(LOCK) {
            Log.d("UserDatabase", "Creating new database instance")
            val newInstance = Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java, "user_db"
            )
                .fallbackToDestructiveMigration()
                .build()
            instance = newInstance
            return newInstance
        }
    }
}




