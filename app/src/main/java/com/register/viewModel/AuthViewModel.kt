package com.register.viewModel

import android.annotation.SuppressLint
import android.database.Observable
import android.text.Editable
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.register.Model.BaseBinding
import com.register.Model.User
import com.register.Repository.UserRepository
import com.register.Utils.AuthListener
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    private var disposables = CompositeDisposable()
    var authListener: AuthListener? = null

    private val user: User by lazy { User(firstName = "", lastName = "", id = null, pin = null) }


    var oFirstName = user.oFirstName
    var oLastName = user.oLastName
    var oUid = user.oUid
    var oPin = user.oPin
    private var oPinChanged: String? = oPin
    
//    Fields
@SuppressLint("CheckResult")
fun registerUser() {

    // First name check
    if (oFirstName.isNullOrEmpty()) {
        authListener?.onFailure("First name Required")
        return
    } else {
        authListener?.onStarted()

    }

    // Last name check
    if (oLastName.isNullOrEmpty()) {
        authListener?.onFailure("Last name is Required")
        return
    } else {
        authListener?.onStarted()

    }

    // ID check
    if (oUid.toString().isEmpty() || !oUid.toString().matches(Regex("^(?!\\s*\$)[0-9\\s]{8}\$"))) {
        authListener?.onFailure("Invalid. ID Must be 8 digits")
        return } else {
        authListener?.onSuccess()
        verifyFieldsAndSave()
    }
}

    @SuppressLint("CheckResult")
    fun setPin(): Boolean{
        val pin1: String? = oPin
        val pin2: String? = oPinChanged
        val pinLength = if (pin1?.length != pin2?.length){
            authListener?.onFailure("Pin has to Match")
            return false

        } else {
            authListener?.onSuccess()
            true
        }
        if (pinLength) {
            val pinArray1 = pin1?.toInt()
            val pinArray2 = pin2?.toInt()
            if (pinArray1 == pinArray2) {
                authListener?.onSuccess()
                return true

            }else {
                authListener?.onFailure("Invalid Confirm Your Entities")
                false

            }
        }
        return pinLength

    }

    fun loginUser() {
        //Pin
        if (!oPin.toString().matches(Regex("^(?!\\s*\$)[0-9\\s]{4}\$")) &&
            oPin.toString().isEmpty()) {
            authListener?.onFailure("Invalid ")
            return } else {
            authListener?.onStarted() }
        return repository.register(user)
    }


//    Setting Details Observable

    fun firstNameChanged(editable: Editable?) {
        Log.d("AuthViewModel", "First name data pipeline")
        val firstName = editable.toString()
        oFirstName = firstName
    return
    }


    fun lastNameChanged(editable: Editable?){
        Log.d("AuthViewModel", "Last name data pipeline")
        val lastName = editable.toString()
        oLastName = lastName
        return
    }


    fun idChanged(editable: Editable?){
            Log.d("AuthViewModel", "id data pipeline")
            val id: String? = editable?.toString()
            oUid = id
        return
    }


    fun pin(editable: Editable?) {
        Log.d("AuthViewModel","Pin confirmation pipeline set")
        val pin: String? = editable?.toString()
        oPin = pin
        return
    }


    fun pinChanged(editable: Editable?){
        Log.d("AuthViewModel", "pin pipeline setup")
        val pinChanged: String? = editable?.toString()
        oPinChanged = pinChanged
        return
    }


    //    Verify fields and Save the data
    private fun verifyFieldsAndSave() {
        val saveData = repository.register(user)

        if (oFirstName.toString().isNotEmpty() || oLastName.toString().isNotEmpty()
            || oUid.toString().isNotEmpty() || oPin.toString().isNotEmpty()) {
            authListener?.onSuccess().apply {
                saveData.toString()

            } }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }



}



