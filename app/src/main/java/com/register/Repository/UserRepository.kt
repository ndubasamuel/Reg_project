package com.register.Repository

import android.annotation.SuppressLint
import android.graphics.Insets.add
import android.util.Log
import com.register.DB.UserDao
import com.register.Model.User
import com.register.Utils.DatabaseEvent
import com.register.Utils.Resource
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Subscriber
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    private val disposables: CompositeDisposable? = null

    private val mObserverSubject = PublishSubject.create<DatabaseEvent<User>>()
    private val mObservableSubject1 = PublishSubject.create<DatabaseEvent<Int>>()


    //  Register Use

    fun insert(user: User){
       Completable.fromPublisher<Pair<User?, Int?>> {
            userDao.insert(user)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
           .doOnError{
               Log.d("Repository", "User Register: $it")
           }
           .doFinally {
               disposables?.clear()
           }
           .subscribe()
    }


//    fun insert(user: User) {
//        Flowable.fromPublisher<Pair<User?, User>> {
//            userDao.insert(user)
//            Log.d("Repository", "Room Action: $user")
//        }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnError { error ->
//                Log.e("Repository", "Encountered an error: $error")
//            }
//            .doOnSubscribe { disposable ->
//                disposables?.add(disposable)
//            }
//            .doFinally {
//                disposables?.clear()
//            }
//            .subscribe()
//    }

    //  Login User
    fun login(int: Int): Flowable<Int> {
        val loginUser = DatabaseEvent(DatabaseEventType.VERIFY, int)
        return userDao.login(int)
            .doOnEach { mObservableSubject1.onNext(loginUser) }
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
    fun observeText(): Observable<DatabaseEvent<String>> {
        return userDao.getGreeting("")
            .flatMapObservable { greeting ->
                Observable.just(DatabaseEvent(DatabaseEventType.INSERTED, greeting))
            }
            .onErrorResumeNext(Observable.empty())
            .doOnError { error ->
                Log.e("Repository", "Encountered an error: $error")
            }
            .doOnSubscribe { disposable ->
                disposables?.add(disposable)
            }
            .doFinally {
                disposables?.clear()
            }
    }

    //  Observe all Users
    @SuppressLint("CheckResult")
    fun observeUser(firstName: String): Flowable<String> {
        return userDao.getName(firstName)
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



