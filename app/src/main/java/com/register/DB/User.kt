package com.register.DB

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: Int,

    var firstName: String = "",
    var lastName: String = "",
    val pin: Int? = null

) : BaseObservable() {

    @get: Bindable
    @Ignore
    var oFirstName: String = ""
        set(value) {
            field = value
            firstName = value
        }

    @get : Bindable
    @Ignore
    var oLastName: String = ""
        set(value) {
            field = value
            lastName = value
        }
    @get : Bindable
    @Ignore
    var oUid: Int? = null
        set(value) {
            if (value != null) {
                id = value
            }
            field = value
        }
}
