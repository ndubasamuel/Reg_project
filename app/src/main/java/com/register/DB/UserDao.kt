package com.register.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.register.Model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun upsert(users: User) : Completable

    @Query("SELECT pin FROM users WHERE pin = :pin")
    fun login(pin: Int?): Flowable<Int>

    @Query("SELECT greeting FROM users WHERE greeting = :greeting")
    fun getGreeting(greeting: String): Single<String>

    @Query("SELECT firstname FROM users WHERE firstName = :firstName")
    fun getName(firstName: String): Flowable<String>






}