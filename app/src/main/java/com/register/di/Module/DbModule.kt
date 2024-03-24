package com.register.di.Module

import android.app.Application
import androidx.room.Room
import com.register.DB.UserDao
import com.register.DB.UserDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DbModule(MyApplication: Application) {

    private val userDataBase: UserDataBase =
        Room.databaseBuilder(MyApplication, UserDataBase::class.java, "user_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    internal fun provideRoomDatabase() : UserDataBase {
        return userDataBase
    }

    @Singleton
    @Provides
    internal fun provideUserDao() : UserDao {
        return userDataBase.userDao()

    }
}
