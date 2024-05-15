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
import io.reactivex.schedulers.Schedulers
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

                    userId = it1,
                    firstName = firstName,
                    lastName = lastName,
                    pin = 0 )

        }
        Log.d("ViewModel onClickReg", "User: $user")
        return Flowable.just(Resource.Success(data = user))

    }

    fun onClickPin(): Flowable<Resource<User>> {
        val pin = oPin.get() ?: ""

        if (pin.isEmpty() || !pin.matches(Regex("^(?!\\s*\$)[0-9\\s]{4}\$"))) {
            return Flowable.just(Resource.Error("Pin Required: Must be 4 Digits"))
        }
        val pin2 = oPinChanged.get() ?: ""
        if (pin2.isEmpty() || !pin2.matches(Regex("^[0-9]{4}\$")) || pin != pin2) {
            return Flowable.just(Resource.Error("Confirm Pin"))
        }

        val user = User(
                    0,
                    "",
                    "",
                     pin = pin.toIntOrNull())


        Log.d("ViewModel onClickPin", "User: $user")
        return Flowable.just(Resource.Success(data = user))
    }


    val registerUser by lazy {
         Flowable.zip(onClickReg(), onClickPin(), { resourceReg, resourcePin ->
            Pair(resourceReg, resourcePin)
        })
            .flatMap { (resourceReg, resourcePin) ->
                val userDataReg = resourceReg.data
                val userDataPin = resourcePin.data


                val userRegFlowable = userDataReg?.let { user ->
                    Log.d("ViewModel", "UserRegFlow DATA: $user")
                    Flowable.just(listOf(user.userId, user.firstName, user.lastName))

                } ?: Flowable.empty()

                val userPinFlowable = userDataPin?.let { it ->
                    Log.d("ViewModel", "UserPinFlow DATA: $it")
                    Flowable.just(it.pin)

                } ?: Flowable.empty<Int>()


                Flowable.zip(userRegFlowable, userPinFlowable,
                    BiFunction<List<out Any?>, Int, User> { userData, pin ->
                        Log.d("ViewModel Bi-Function", "User Data: $userData, $pin")
                        val userId = userData[0] as Int
                        val firstName = userData[1] as String
                        val lastName = userData[2] as String
                        User(userId, firstName, lastName, pin)

                    })
//                Flowable.zip(
//                    userPinFlowable as Publisher<*>?,
//                    userRegFlowable as Publisher<*>?
//                ) {
//                   _, _ ->
//                    Log.d("ViewModel", "Flowable Zip data: $userRegFlowable")
////                    Pair(userDataReg, userDataPin)
//                }
            }
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
             .doOnNext { it ->
                 Log.d("ViewModel", "ViewModel Data Reg: ${it}")
                 Log.d("ViewModel", "ViewModel Data Pin: ${it}")
             }
             .doOnCancel {
                 disposables.clear()
             }
//             .debounce  (10L, TimeUnit.SECONDS)
            .subscribe({
                repository.insert(it)
                Log.d("ViewModel Subscribe", "ViewModel Data Reg: ${it}")
                Log.d("ViewModel Subscribe", "ViewModel Data Pin: ${it}")
            }, { error ->
                // Handle error here
                Log.e("ViewModel", "Reg Failed: $error")
            })

    }
//    private fun serialize(comparable: Comparable<*>?): ByteArray {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
//        objectOutputStream.writeObject(comparable)
//        objectOutputStream.close()
//        return byteArrayOutputStream.toByteArray()
//    }


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





