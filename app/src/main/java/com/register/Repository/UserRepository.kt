package com.register.Repository

import android.annotation.SuppressLint
import android.util.Log
import com.register.DB.UserDao
import com.register.Model.User
import com.register.Utils.DatabaseEvent
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    private val disposables: CompositeDisposable? = null

    private val mObserverSubject = PublishSubject.create<DatabaseEvent<User>>()
    private val mObservableSubject1 = PublishSubject.create<DatabaseEvent<Int>>()


    //  Register Use
    @SuppressLint("CheckResult")
    fun insert(user: User) {
       Completable.fromAction{
           Log.d("Repository", "User inserted successfully")
           userDao.insert(user)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("Repository", "User inserted successfully")
            }, { error ->
                Log.e("Repository", "Failed to insert user: $error")
            })
    }



    //  Login User
    fun login(int: Int): Maybe<Int> {
        return userDao.login(int)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error ->
                Log.e("Repository", "Encountered an error: $error")
            }
            .doOnSubscribe {
            }
            .doFinally {
                disposables?.clear()
            }
    }

    //  Observe Greeting text
//    fun observeText() : Single<String>{
//        Single.fromCallable{
//            userDao.getGreeting("")
//        }
//            .observeOn(Schedulers.io())
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { disposable ->
//                disposables?.add(disposable)
//            }
//            .subscribe({
//
//            }, { error ->
//                Log.d("Repo", "Room text error: $error")
//            }).dispose()
//
//           return Single.just("")
//    }

    //  Observe all Users
    @SuppressLint("CheckResult")
    fun getAllUsers(): Flowable<List<User>> {
        return userDao.getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error ->
                Log.e("Repository", "Encountered an error: $error")
            }
            .doOnSubscribe {

            }
            .doFinally {
                disposables?.clear()
            }
    }
}



