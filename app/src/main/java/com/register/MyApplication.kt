package com.register

import android.app.Application
import com.register.di.Component.AppComponent
import com.register.di.Component.DaggerAppComponent
import com.register.di.Module.DbModule
import javax.inject.Qualifier


class MyApplication : Application(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initializeComponent()
         setInstance(this)
    }
        private fun initializeComponent() {
            appComponent = DaggerAppComponent.builder()
                .application(this)
                .dbModule(DbModule(this))
                .build()
            appComponent.inject(this)
    }
    companion object {
        private var appContext: MyApplication? = null
            private set


        @Synchronized
        private fun setInstance(app: MyApplication) {
            appContext = app
        }

    }


}


