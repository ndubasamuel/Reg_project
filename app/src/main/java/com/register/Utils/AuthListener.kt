package com.register.Utils


interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)

}