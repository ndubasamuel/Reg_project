package com.register.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.register.Model.User
import com.register.Model.UserAdapter.Navigator
import com.register.Repository.UserRepository
import com.register.Utils.DatabaseEvent
import com.register.Utils.Resource
import com.register.Utils.StreamListener
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private var disposables = CompositeDisposable()

    val oFirstName = ObservableField<String>("")
    val oLastName = ObservableField<String>("")
    val oUid = ObservableField<String>("")
    val oPin = ObservableField<String>("")
    val oPinChanged = ObservableField<String>("")


    fun getUserName(firstName: String): Flowable<String>{
        return repository.observeUser(firstName)
    }
    fun helloText(): Observable<DatabaseEvent<String>> {
        Log.d("ViewModel", "Room String $String")
        return repository.observeText()
    }
    fun onClickReg(): Flowable<Resource<User>> {
        val firstName = oFirstName.get() ?: ""
        val lastName = oLastName.get() ?: ""
        val id = oUid.get() ?: ""
        val pin = oPin.get() ?: ""

        if (firstName.isEmpty()) {
            return Flowable.just(Resource.Error("First name Required"))
        }
        // Last name check

        if (lastName.isEmpty()) {
            return Flowable.just(Resource.Error("Last name Required"))
        }

        if (id.isEmpty() || !id.matches(Regex("^(?!\\s*\$)[0-9\\s]{8}\$")) ) {
            return Flowable.just(Resource.Error("Invalid. ID Must be 8 digits"))
        }

        val user = id.toIntOrNull()?.let { it1 ->
                User(
                    0,
                    userId = it1,
                    firstName = firstName,
                    lastName = lastName,
                    pin = pin.toIntOrNull() )

        }
        Log.d("ViewModel onClickReg", "User: $user")
        return Flowable.just(Resource.Success(data = user))

    }

    fun onClickPin(): Flowable<Resource<User>> {
        val firstName = oFirstName.get() ?: ""
        val lastName = oLastName.get() ?: ""
        val id = oUid.get() ?: ""

        val pin = oPin.get() ?: ""

        if (pin.isEmpty() || !pin.matches(Regex("^(?!\\s*\$)[0-9\\s]{4}\$"))) {
            return Flowable.just(Resource.Error("Pin Required: Must be 4 Digits"))
        }
        val pin2 = oPinChanged.get() ?: ""
        if (pin2.isEmpty() || !pin2.matches(Regex("^[0-9]{4}\$")) || pin != pin2) {
            return Flowable.just(Resource.Error("Confirm Pin"))
        }

        val user = pin.toIntOrNull()?.let {it ->
            id.toIntOrNull()?.let { it1 ->
                User(
                    0,
                    userId = it1,
                    firstName = firstName,
                    lastName = lastName,
                    pin = it, )
            }
        }

        Log.d("ViewModel", "User: $user")
        return Flowable.just(Resource.Success(data = user))
    }





    val registerUser by lazy {
        Flowable.zip(onClickReg(), onClickPin(), { resourceReg, resourcePin ->
            Pair(resourceReg, resourcePin)
        })
            .flatMap { (resourceReg, resourcePin) ->
                val userDataReg = resourceReg.data
                val userDataPin = resourcePin.data
                var userPin: Int? = null
                var userRegFlowable: Flowable<User>? = null


                val userPinFlowable = userDataPin?.let {
                    userPin = it.pin
                    userRegFlowable = userDataReg?.let {
                        repository.insert(it) } ?: Flowable.empty<User>()
                     } ?: Flowable.empty<User>()



                Flowable.zip(userRegFlowable as Publisher<out User>?,
                    userPinFlowable as Publisher<out User>?, { _, _ -> Pair(userDataReg, userDataPin) })
            }
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ (userDataReg, userDataPin) ->
                // Handle success here
                Log.d("ViewModel", "ViewModel Data Reg: $userDataReg")
                Log.d("ViewModel", "ViewModel Data Pin: $userDataPin")
            }, { error ->
                // Handle error here
                Log.e("ViewModel", "Reg Failed: $error")
            })
    }


    //loginPin_Setup
    fun loginUser(): Flowable<Resource<Int>> {
        val pin = oPin.get() ?: ""

        if (pin.isEmpty() || !pin.matches(Regex("^[0-9]{4}$"))) {
            return Flowable.just(Resource.Error<Int>("User Invalid Pin"))
        } else {
            return repository.login(0)
                .flatMap { id -> Flowable.just(Resource.Success(id)) }
//                .startWith(Resource.Loading())
        }

    }


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}





