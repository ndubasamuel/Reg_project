package com.register.viewModel

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.room.Ignore
import com.register.DB.User
import com.register.Repository.UserRepository
import com.register.Utils.AuthListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {


    var oFirstName = ""
    var oLastName = ""
    var oUid: Int? = null


    private val disposables = CompositeDisposable()
    var authListener: AuthListener? = null

    fun registerUser() {
        if (oFirstName.isEmpty() || oLastName.isEmpty()) {
            authListener?.onFailure("Please enter both first name and last name")
            return
        }

            val user = User (
                firstName = oFirstName,
                lastName = oLastName,
                id = oUid !!
            )

        disposables.add(
            repository.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        authListener?.onSuccess()},
                    {
                        authListener?.onFailure("")?.let { error(it) }
                    }
                )
        )

    }



}