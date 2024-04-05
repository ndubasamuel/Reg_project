package com.register.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.register.Model.User


@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UserDataBase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var instance: UserDataBase? = null
        private val LOCK = Any()
    }

    operator fun invoke(context: Context) = instance ?: kotlin.synchronized(LOCK) {
        instance ?: db(context).also { instance = it}
    }

    private fun db(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        UserDataBase::class.java, "user_db"
    ).build()
}
