package com.register.Repository

import android.annotation.SuppressLint
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
    private val disposable: CompositeDisposable? = null

    private val mObserverSubject = PublishSubject.create<DatabaseEvent<User>>()
    private val mObservableSubject1 = PublishSubject.create<DatabaseEvent<Int>>()


    //  Register Use

    @SuppressLint("CheckResult")
    fun insert(user: User) {
        Completable.fromCallable {
            Log.d("Repository", "Room Action")
            userDao.insert(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
//                    onComplete
                },
                {error ->
                    Log.d("Repository", "Encountered an error: $error")
                }
            )
    }

    //    Login User
    @SuppressLint("CheckResult")
    fun login(int: Int): Flowable<Int>{
        val loginUser = DatabaseEvent(DatabaseEventType.VERIFY, int)
        return userDao.login(int)
        .doOnEach { mObservableSubject1.onNext(loginUser)
            Log.d("Repository", "User Login")
        }
    }
//    Observe Greeting text
    fun observeText(): Observable<DatabaseEvent<String>> {
        Log.d("Repo", "Greeting")
       return userDao.getGreeting("")
           .flatMapObservable { greeting ->
               Observable.just(DatabaseEvent(DatabaseEventType.INSERTED, greeting))
           }
           .onErrorResumeNext(Observable.empty())
           .doOnError { Log.d("Repository", "User Fetch Failed") }
    }

//    Observe all Users
    fun observeUser(firstName: String): Maybe<DatabaseEvent<String>> {
        Log.d("Repo", "All Users ")
       return userDao.getName(firstName)
           .flatMap { userDao.getName(firstName) }
           .map { DatabaseEvent(DatabaseEventType.RETRIEVED, firstName) }
           .doOnTerminate{(mObserverSubject)}
    }
}

