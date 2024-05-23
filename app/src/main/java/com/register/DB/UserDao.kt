package com.register.DB

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.register.Model.User
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT pin FROM users WHERE pin = :pin")
    fun login(pin: Int?): Maybe<Int>

    @Query("SELECT greeting FROM users WHERE greeting = :greeting")
    fun getGreeting(greeting: String): Single<String>

    @Query("SELECT * FROM users ORDER BY firstName DESC LIMIT 1")
    fun getAllUsers(): Flowable<List<User>>









}