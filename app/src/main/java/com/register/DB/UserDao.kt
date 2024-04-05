package com.register.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.register.Model.User
import io.reactivex.Completable
import io.reactivex.Flowable


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: User) : Completable


}