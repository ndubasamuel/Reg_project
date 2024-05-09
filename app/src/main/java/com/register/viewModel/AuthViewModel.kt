package com.register.viewModel

import android.annotation.SuppressLint
import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.register.Model.User
import com.register.Model.UserAdapter.Navigator
import com.register.Repository.UserRepository
import com.register.Utils.DatabaseEvent
import com.register.Utils.Resource
import com.register.Utils.StreamListener
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import java.lang.Boolean.valueOf
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private var disposables = CompositeDisposable()
    var streamListener: StreamListener? = null
    var navigator: Navigator? = null

    var oFirstName = ""
    var oLastName = ""
    var oUid = ""
    var oPin = ""
    var oPinChanged: String? = oPin

    fun firstNameChanged(editable: Editable?) {
        val fName = editable.toString()
        oFirstName = fName
        return
    }
    fun lastNameChanged(editable: Editable?) {
        val lName = editable.toString()
        oLastName = lName
        return
    }
    fun idChanged(editable: Editable?) {
        val id = editable.toString()
        oUid = id
        return
    }
    fun pinInput(editable: Editable?) {
        val pin = editable.toString()
        oPin = pin
        return
    }
    fun pinChanged(editable: Editable?) {
        val pinChanged = editable.toString()
        oPinChanged = pinChanged
        return
    }

    fun getUserName() : Maybe<DatabaseEvent<String>>{
        return repository.observeUser("")
    }

    fun helloText(): Observable<DatabaseEvent<String>> {
        Log.d("ViewModel", "Room String $String")
        return repository.observeText()
    }
    @SuppressLint("CheckResult", "SuspiciousIndentation")
    fun onClickReg(): Boolean {
        streamListener?.onStarted()

        if (oFirstName.isEmpty()) {
            streamListener?.onFailure("First name Required")
            return false
        } else {
            Log.d("ViewModel name", "FirstName $oFirstName")
            streamListener?.onStarted()
        }
        // Last name check
        if (oLastName.isEmpty()) {
            streamListener?.onFailure("Last name is Required")
            return false
        } else {
            Log.d("ViewModel Name", " LastName $oLastName")
            streamListener?.onStarted()
        }

        if (oUid.isNullOrBlank() && !oUid.matches(Regex("^(?!\\s*\$)[0-9\\s]{8}\$"))) {
            streamListener?.onFailure("Invalid. ID Must be 8 digits")
            return false
        } else {
            streamListener?.onStarted()
            Log.d("ViewModel Name", "ID $oUid")
        }
        val userId = oUid.toIntOrNull()
        if (userId == null) {
            streamListener?.onFailure("Invalid User ID")
        }



            val user = User(id = oUid.toInt(), firstName = oFirstName, lastName = oLastName, pin = oPin.length)

            val regUser = Flowable.create<Resource<User>>({ emitter ->
                emitter.onNext(Resource.Loading())
                try {
                    emitter.onNext(Resource.Success(user))
                    Log.d("ViewModel", "User upstream")
                    emitter.onComplete()
                } catch (e: Exception) {
                    Log.e("ViewModel", "User Registration Failed: ${e.message}")
                    emitter.onError(e)
                }
            }, BackpressureStrategy.BUFFER)


            disposables.add(
                regUser.subscribe({ resource ->
                    Log.d("ViewModel", "Down Stream: ${resource.data}")
                    when (resource) {
                        is Resource.Loading -> Log.d("ViewModel", "Loading")
                        is Resource.Success -> resource.data?.apply {
                            repository.insert(user)

                            navigator?.navigateToPinFragment()

                            Log.d("ViewModel", "Registration Went Through $user")
                        }

                        is Resource.Error -> Log.e("ViewModel", "Registration Failed: ${resource.message}")
                    }

                }, { error ->
                    Log.e("ViewModel", "Down Stream Failed: ${error.message}")
                })
            )

            streamListener?.onSuccess()
            return true
        }


    @SuppressLint("CheckResult")
    fun onClickPin(): Boolean {
        streamListener?.onStarted()
        val pin1 = oPin
        val pin2 = oPinChanged

        if (pin1.isNullOrBlank() || !pin1.matches(Regex("^[0-9]{4}\$"))) {
            streamListener?.onFailure("Pin Required: Must be 4 Digits")
            return false
        } else {
            streamListener?.onStarted()
        }
        if (pin2!!.isNullOrBlank() || !pin2.matches(Regex("^[0-9]{4}\$"))) {
            streamListener?.onFailure("Pin Confirmation Required")
            return false
        } else {
            streamListener?.onStarted()
        }

        if (pin1.length != pin2.length) {
            streamListener?.onFailure("Pin has to Match")
            return false

        } else if (pin1 == pin2) {
            val user = User(id = oUid.toInt(), firstName = oFirstName, lastName = oLastName, pin = oPin.length)

            val regUser = Flowable.create<Resource<User>>({ emitter ->
                emitter.onNext(Resource.Loading())
                try {
                    emitter.onNext(Resource.Success(user))
                    Log.d("ViewModel", "User upstream")
                    emitter.onComplete()
                } catch (e: Exception) {
                    Log.e("ViewModel", "User Registration Failed: ${e.message}")
                    emitter.onError(e)
                }
            }, BackpressureStrategy.BUFFER)

            disposables.add(
                regUser.subscribe({ resource ->
                    Log.d("ViewModel", "Down Stream: ${resource.data}")
                    when (resource) {
                        is Resource.Loading -> Log.d("ViewModel", "Loading")
                        is Resource.Success -> resource.data?.apply {
                            repository.insert(user)

                            Log.d("ViewModel", "Registration Went Through $user")
                        }

                        is Resource.Error -> Log.e("ViewModel", "Registration Failed: ${resource.message}")
                    }

                }, { error ->
                    Log.e("ViewModel", "Down Stream Failed: ${error.message}")
                })
            )

            streamListener?.onSuccess()
            return true
        } else {
            streamListener?.onFailure("Invalid: Confirm Your Entities")
        }


        streamListener?.onSuccess()
        return true



    }

    //loginPin_Setup
    fun loginUser(): Flowable<Resource<Int>> {
        val pin: Int = oPin!!.toInt()

        if (pin.toString().isEmpty() || !pin.toString().matches(Regex("^[0-9]{4}$"))) {
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




