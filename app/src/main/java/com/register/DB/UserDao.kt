package com.register.DB

import android.provider.SyncStateContract.Helpers.insert
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.register.Model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT pin FROM users WHERE pin = :pin")
    fun login(pin: Int?): Flowable<Int>

    @Query("SELECT greeting FROM users WHERE greeting = :greeting")
    fun getGreeting(greeting: String): Single<String>

    @Query("SELECT firstname FROM users WHERE firstName = :firstName")
    fun getName(firstName: String): Maybe<String>






}