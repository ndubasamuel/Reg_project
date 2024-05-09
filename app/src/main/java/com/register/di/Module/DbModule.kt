package com.register.di.Module

import android.app.Application
import androidx.room.Room
import com.register.DB.UserDao
import com.register.DB.UserDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DbModule(MyApplication: Application) {

    private val userDatabase: UserDatabase =
        Room.databaseBuilder(MyApplication, UserDatabase::class.java, "user_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    internal fun provideRoomDatabase() : UserDatabase {
        return userDatabase
    }

    @Singleton
    @Provides
    internal fun provideUserDao() : UserDao {
        return userDatabase.userDao()

    }
}
