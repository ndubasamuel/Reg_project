package com.register.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.register.Model.User
import com.register.Repository.UserRepository
import com.register.Utils.DatabaseEvent
import com.register.Utils.Resource
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.asFlow
import org.reactivestreams.Publisher
import javax.inject.Inject


@SuppressLint("CheckResult")
class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private var disposables = CompositeDisposable()

    val oFirstName = ObservableField<String>("")
    val oLastName = ObservableField<String>("")
    val oUid = ObservableField<String>("")
    val oPin = ObservableField<String>("")
    val oPinChanged = ObservableField<String>("")

    init {
        disposables.add(getUsers().subscribe())

    }


    fun getUsers(): Flowable<List<User>>{
        return repository.getAllUsers()
    }
//    fun helloText(): Single<String> {
////        Log.d("ViewModel", "Room String $String")
//        return repository.observeText()
//    }



    val onClickReg: () -> Single<Resource<List<String>>> = {
        val firstName = oFirstName.get() ?: ""
        val lastName = oLastName.get() ?: ""
        val id = oUid.get() ?: ""

        if (firstName.isEmpty()) {
            Single.just(Resource.Error("First name is required"))
        } else if (lastName.isEmpty()) {
            Single.just(Resource.Error("Last name is required"))
        } else if (id.isEmpty() || !id.matches(Regex("^[0-9]{8}$"))) {
            Single.just(Resource.Error("Invalid ID: must be 8 digits"))
        } else {
            val userData: List<String> = listOf(firstName, lastName, id)
            Single.just(Resource.Success(data = userData))
        }
    }


    val onClickPin: () -> Single<Resource<String>> = {
        val pin = oPin.get() ?: ""
        if (pin.isEmpty() || !pin.matches(Regex("^[0-9]{4}$"))) {
            Single.just(Resource.Error("Pin Required: Must be 4 digits"))
        } else {
            val pin2 = oPinChanged.get() ?: ""
            if (pin2.isEmpty() || pin2 != pin) {
                Single.just(Resource.Error("Confirm Pin"))
            } else {
                val userPin: String = pin
                Single.just(Resource.Success(data = userPin))
            }
        }
    }

    fun <onClickReg, onClickPin> Single<onClickReg>.zipWith(other: SingleSource<onClickPin>): Single<Pair<onClickReg, onClickPin>> {
        return this.zipWith(other, BiFunction<onClickReg, onClickPin, Pair<onClickReg, onClickPin>>{ regResource, pinResource ->
            Pair(regResource, pinResource)
        })
    }

    fun registerUser() {
         Single.zip(
            /* source1 = */ onClickReg(),
            /* source2 = */ onClickPin(),
            /* zipper = */
            io.reactivex.functions.BiFunction<Resource<List<String>>, Resource<String>, User>
            { userRegRes, userPinRes ->
                Log.d("View Model", "User Data: ${userRegRes.data} ${userPinRes.data}")
                Pair(userRegRes, userPinRes)

                val userReg = userRegRes.data
                Log.d("View Model", "User Data Reg:  $userReg")
                val userPin = userPinRes.data
                Log.d("View Model", "User Data Pin: $userPin")

                val firstName = userReg?.get(0) as String
                val lastName = userReg[1]
                val userId = userReg[2]

               val regUser = User(userId.toInt(), firstName, lastName, userPin.toString().toIntOrNull())
                Log.e("viwmodel", regUser.toString() )
                regUser
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { user ->
                Log.d("View Model User Room Upsert", "User Data: $user")
                if (user != null) {
                    repository.insert(user)
                }
            }.subscribe({

             }, {error ->
                 Log.d("ViewModel Data Upsert", "User Error: $error")
             }
            )
    }


    //loginPin_Setup
    fun loginUser(): Maybe<Int> {
        Log.d("ViewModel", "Login User")
        val pin = oPin.get() ?: ""

        if (pin.isEmpty() || !pin.matches(Regex("^[0-9]{4}$"))) {
            return Maybe.empty()
        } else {
            return repository.login(0)

        }
    }


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
