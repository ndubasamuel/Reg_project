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
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@SuppressLint("CheckResult")
class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private var disposables = CompositeDisposable()
    var streamListener: StreamListener? = null


    var oFirstName: String? = ""
    var oLastName: String? = ""
    var oUid: String? = ""
    var oPin: String?= ""
    var oPinChanged: String? = ""

    private val user = User(firstName = oFirstName.toString(), lastName = oLastName.toString(), id = null, pin = null)

    private val regUser = Flowable.create({ emitter ->
        emitter.onNext(Resource.Loading())
        val registerUser = repository.register(user)
        Log.d("ViewModel", "User Registration $user")
        emitter.onNext(Resource.Success(registerUser))
        emitter.onComplete()
    }, BackpressureStrategy.BUFFER)


    init {
        regUser.subscribe ({ resource ->
            if (Resource.Success(data = user) == resource.data) {
                Log.d("ViewModel", "Registration Successful")
                 repository.register(user)
            } }, { error ->
            Log.e("ViewModel", "Error")
        })
    }


    fun helloText(): Observable<DatabaseEvent<String>> {
        Log.d("ViewModel", "Room String $String")
        return repository.observeText()
    }

//    fun getUser(firstName: String): Flowable<DatabaseEvent<String>> {
//        Log.d("ViewModel", "User $firstName")
//        return
//    }

    @SuppressLint("CheckResult")
    fun onClickReg() : Boolean {
        streamListener?.onStarted()

        if (oFirstName?.isEmpty() == true) {
            streamListener?.onFailure("First name Required")
            return false
        } else {
            streamListener?.onStarted()
        }

        // Last name check
        if (oLastName?.isEmpty() == true) {
            streamListener?.onFailure("Last name is Required")
            return false
        } else {
            streamListener?.onStarted()
        }

        // ID check
        if (oUid.toString().isEmpty() || !oUid.toString().matches(Regex("^(?!\\s*\$)[0-9\\s]{8}\$"))) {
            streamListener?.onFailure("Invalid. ID Must be 8 digits")
            return false
        }
        streamListener?.onSuccess()
        return true

    }

    fun onClickPin(): Boolean {

        streamListener?.onStarted()

        val pin1 = oPin
        val pin2 = oPinChanged
        if (pin1.toString().isEmpty() || !pin1.toString().matches(Regex("^[0-9]{4}\$"))) {
            streamListener?.onFailure("Pin Required: Must be 4 Digits")
            return false
        } else {
            Resource.Success(user)

        }

        if (pin2.toString().isEmpty() || !pin2.toString().matches(Regex("^[0-9]{4}\$"))) {
            streamListener?.onFailure("Pin Confirmation Required")
            return false
        } else {
            Resource.Success(user)
        }

        if (pin1.toString().length != pin2.toString().length) {
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
        val pin: Int? = oPin?.toInt()

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
        val firstName = editable.toString()
        oFirstName = firstName
        return
    }


        fun lastNameChanged(editable: Editable?) {
            val lastName = editable.toString()
            oLastName = lastName
            return
        }

        fun idChanged(editable: Editable?) {
            val id: String? = editable?.toString()
            oUid = id !!
            return
        }

        fun pinInput(editable: Editable?) {
            val pin: String? = editable?.toString()
            oPin = pin !!
            return
        }

        fun pinChanged(editable: Editable?) {
            val pinChanged: String? = editable?.toString()
            oPinChanged = pinChanged !!
            return
        }


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}




