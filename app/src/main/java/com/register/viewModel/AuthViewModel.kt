package com.register.viewModel

import android.annotation.SuppressLint
import android.text.Editable
import android.util.Log
import androidx.databinding.library.baseAdapters.BR.*
import androidx.lifecycle.ViewModel
import com.register.Model.User
import com.register.Repository.UserRepository
import com.register.Utils.DatabaseEvent
import com.register.Utils.Resource
import com.register.Utils.StreamListener
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import java.lang.NumberFormatException
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private var disposables = CompositeDisposable()
    var streamListener: StreamListener? = null

    val user = User(null, lastName = "", firstName = null, pin = null)

    var oFirstName = user.oFirstName
    var oLastName = user.oLastName
    var oUid = user.oUid
    var oPin = user.oPin
    private var oPinChanged: String? = oPin


    private val regUser = Flowable.create<Resource<User>>({ emitter ->
        emitter.onNext(Resource.Loading())
        try {

            Log.d("ViewModel", "User Registration Successful: $user")
            emitter.onNext(Resource.Success(user))
            emitter.onComplete()
        } catch (e: Exception) {
            Log.e("ViewModel", "User Registration Failed: ${e.message}")
            emitter.onError(e)
        }
    }, BackpressureStrategy.BUFFER)

    init {
        disposables.add(
            regUser.subscribe({ resource ->
                Log.d("ViewModel", "Down Stream: ${resource.data}")
                when (resource) {
                    is Resource.Loading -> Log.d("ViewModel", "Loading")
                    is Resource.Success -> Log.d("ViewModel", "Registration Successful: ${resource.data}")
                    is Resource.Error -> Log.e("ViewModel", "Registration Failed: ${resource.message}")
                }
            }, { error ->
                Log.e("ViewModel", "Down Stream Failed: ${error.message}")
            })
        )

    }

    fun helloText(): Observable<DatabaseEvent<String>> {
        Log.d("ViewModel", "Room String $String")
        return repository.observeText()
    }

    @SuppressLint("CheckResult")
    fun onClickReg() : Boolean {
        streamListener?.onStarted()

        if (oFirstName!!.isEmpty()) {
            streamListener?.onFailure("First name Required")
            return false
        } else {
            Log.d("ViewModel", "FirstName $oFirstName")
            streamListener?.onStarted()
        }

        // Last name check
        if (oLastName?.isEmpty() == true) {
            streamListener?.onFailure("Last name is Required")
            return false
        } else {
            Log.d("ViewModel Name", " LastName $oLastName")
            streamListener?.onStarted()
        }

        // ID check
        if (oUid!!.isEmpty() || !oUid!!.matches(Regex("^(?!\\s*\$)[0-9\\s]{8}\$"))) {
            streamListener?.onFailure("Invalid. ID Must be 8 digits")
            return false
        } else {
            Log.d("ViewModel ID", "ID $oUid")
            streamListener?.onSuccess()
        }
         repository.register(user)
        return true
    }

    fun onClickPin(): Boolean {

        streamListener?.onStarted()

        val pin1 = oPin
        val pin2 = oPinChanged
        if (pin1!!.isEmpty() || !pin1.matches(Regex("^[0-9]{4}\$"))) {
            streamListener?.onFailure("Pin Required: Must be 4 Digits")
            return false
        }
        if (pin2!!.isEmpty() || !pin2.matches(Regex("^[0-9]{4}\$"))) {
            streamListener?.onFailure("Pin Confirmation Required")
            return false
        }

        if (pin1.length != pin2.length) {
            streamListener?.onFailure("Pin has to Match")
            return false

        }
        if (pin1 == pin2) {
            streamListener?.onSuccess()
        } else {
            streamListener?.onFailure("Invalid: Confirm Your Entities")
        }
//        navigator?.navigateToHomeFragment()
        return true
    }

    //loginPin_Setup
    fun loginUser(): Flowable<Resource<Int>> {
        val pin: Int = oPin!!.toInt()

        if (pin.toString().isEmpty() || !pin.toString().matches(Regex("^[0-9]{4}$"))) {
            Resource.Error<User>("User Invalid Pin")
            return Flowable.empty()
        } else {
            Resource.Loading<User>()
        }

        return repository.login(0).flatMap { id ->
            Flowable.just(Resource.Success(id))
        }
    }
    fun firstNameChanged(editable: Editable?) {
        val firstName = editable


        return
    }
    fun lastNameChanged(editable: Editable?) {
        val lastName = editable.toString()
        oLastName = lastName
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


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}




