package com.register.Utils


interface StreamListener {
    fun onStarted()

    fun onSuccess()
    fun onFailure(message: String)

}