package com.register.Repository

import androidx.databinding.Bindable
import androidx.room.Ignore
import com.register.DB.User
import com.register.DB.UserDao
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import java.util.jar.Attributes.Name
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun login(pin: Int) {


    }

    fun register(user: User): Completable {
        return Completable.fromCallable {
            userDao.insert(user)
        }

    }


}