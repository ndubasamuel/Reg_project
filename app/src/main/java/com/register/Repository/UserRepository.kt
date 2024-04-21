package com.register.Repository

import android.util.Log
import com.register.DB.UserDao
import com.register.Model.User
import com.register.Utils.DatabaseEvent
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    private val mObserverSubject = PublishSubject.create<DatabaseEvent<User>>()
//    private val mObservableSubject = PublishSubject.create<DatabaseEvent<String>>()
    private val mObservableSubject1 = PublishSubject.create<DatabaseEvent<Int>>()


    //  Register User
fun register(user: User): Completable {
    val insertUser = DatabaseEvent(DatabaseEventType.INSERTED, user)
    return Completable.fromAction {
        userDao.upsert(user)
        Log.d("Repository", "Do onComplete Action $user")
        mObserverSubject.onNext(insertUser)
    }.subscribeOn(Schedulers.io())
        .doOnError{ it ->
            Log.d("Error", "Handling $it")}

}

    //    Login User
    fun login(int: Int) : Flowable<Int>{
        val loginUser = DatabaseEvent(DatabaseEventType.VERIFY, int)
    return userDao.login(int)
        .doOnEach { mObservableSubject1.onNext(loginUser)
            Log.d("Repository", "User Login (Confirm pin and proceed)")
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
//    fun observeUser(firstName: String): Flowable<DatabaseEvent<String>>? {
//        Log.d("Repo", "All Users ")
//       return userDao.getName(firstName)
//           .flatMap { userDao.getName(firstName) }
//           .map { DatabaseEvent(DatabaseEventType.RETRIEVED, firstName) }
//           .doOnTerminate{(mObserverSubject)}
//    }
}

