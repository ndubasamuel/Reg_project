package com.register.Repository

import android.util.Log
import com.register.Model.User
import com.register.DB.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

//    fun login(pin: Int?): Completable{
//        return Completable.fromCallable{
//            userDao.insert(User(0, "", "", pin))
//        }
//    }

//    fun register(id: Int?, firstName: String, lastName: String) {
//        Log.d("Repository", "Reg Successful ")
////        return Completable.fromCallable {
//            userDao.insert(User(id, firstName, lastName, 0))
//        }



    fun register(user: User){
//        return Completable.fromCallable{
            userDao.insert(user)
        }
    }


//}