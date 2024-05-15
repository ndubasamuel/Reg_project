package com.register.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber


@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "userId")
    var userId: Int ,
    @ColumnInfo(name = "firstName")
    var firstName: String,

    @ColumnInfo(name = "lastName")
    var lastName: String,

    @ColumnInfo(name = "pin")
    var pin: Int? ,

    @ColumnInfo(name = "greeting")
    var greeting: String = "Hello and Welcome"
) : Parcelable

//    : BaseObservable(){
//    @get: Bindable
//    @Ignore
//    var oFirstName: String = ""
//        set(value) {
//            Log.d("User", "Got user $oFirstName")
//            field = value
//            firstName = value.toString()
//
//            notifyPropertyChanged(BR.oFirstName)
//        }
//
//    @get : Bindable
//    @Ignore
//    var oLastName: String = ""
//        set(value) {
//            Log.d("User", "Got User $oLastName")
//            field = value
//            lastName = value
//            notifyPropertyChanged(BR.oLastName)
//        }
//
//    @get : Bindable
//    @Ignore
//    var oUid: String = ""
//        set(value) {
//            Log.d("User", "User ID $oUid")
//            id = value.toInt()
//            field = value
//            notifyPropertyChanged(BR.oUid)
//        }
//
//    @get : Bindable
//    @Ignore
//    var oPin: String? = null
//        set(value) {
//            pin = value?.toInt()
//            field = value
//            notifyPropertyChanged(BR.oPin)
//        }
//
//    @get: Bindable
//    @Ignore
//    var oPinChanged: String? = null
//        set(value) {
//            oPinChanged = value.toString()
//            field = value
//            notifyPropertyChanged(BR.oPinChanged)
//        }
//
//    @get: Bindable
//    @Ignore
//    var dbText = null
//        set(value) {
//            greeting = value.toString()
//            field = value
//        }
//}


