package com.register.Model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.register.BR


@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "first_name")
    var firstName: String = "",
    @ColumnInfo(name = "last_name")
    var lastName: String = "",
    @ColumnInfo(name = "pin")
    var pin: Int? = null

//    val isRegistered: Boolean.Companion

) : BaseObservable() {
    @get: Bindable
    @Ignore
    var oFirstName: String? = ""
        set(value) {
            field = value
            firstName = value.toString()
            notifyPropertyChanged(BR.oFirstName)
        }

    @get : Bindable
    @Ignore
    var oLastName: String? = ""
        set(value) {
            field = value
            lastName = value.toString()
            notifyPropertyChanged(BR.oLastName)
        }
    @get : Bindable
    @Ignore
    var oUid: String? = null
        set(value) {
            id = value.toString().length
            field = value
            notifyPropertyChanged(BR.oUid)
        }
    @get : Bindable
    @Ignore
    var oPin: String? = ""
        set(value) {
            pin = value.toString().length
            field = value
            notifyPropertyChanged(BR.oPin)
        }

}
