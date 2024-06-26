package com.register.Model

import android.text.Editable
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.register.viewModel.AuthViewModel
import io.reactivex.Observable


@Entity(tableName = "users")
data class User(
    @PrimaryKey
    var id: Int? = null,
    @ColumnInfo(name = "firstName")
    var firstName: String? = "",
    @ColumnInfo(name = "lastName")
    var lastName: String? = "",
    @ColumnInfo(name = "pin")
    var pin: Int? = null,

    @ColumnInfo(name = "greeting")
    var greeting: String = "Hello and Welcome"

) : BaseObservable(){
    @get: Bindable
    @Ignore
    var oFirstName: String = ""
        set(value) {
            Log.d("User", "Got user $oFirstName")
            field = value
            firstName = value.toString()

            notifyPropertyChanged(BR.oFirstName)
        }

    @get : Bindable
    @Ignore
    var oLastName: String? = ""
        set(value) {
            Log.d("User", "Got User $oLastName")
            field = value
            lastName = value.toString()
            notifyPropertyChanged(BR.oLastName)
        }

    @get : Bindable
    @Ignore
    var oUid: String? = null
        set(value) {
            Log.d("User", "User ID $oUid")
            id = value.toString().length
            field = value
            notifyPropertyChanged(BR.oUid)
        }

    @get : Bindable
    @Ignore
    var oPin: String? = null
        set(value) {
            pin = value?.toInt()
            field = value
            notifyPropertyChanged(BR.oPin)
        }

    @get: Bindable
    @Ignore
    var oPinChanged: String? = null
        set(value) {
            oPinChanged = value.toString()
            field = value
            notifyPropertyChanged(BR.oPinChanged)
        }

    @get: Bindable
    @Ignore
    var dbText = null
        set(value) {
            greeting = value.toString()
            field = value
        }




}


