package com.register.di.Component

import android.app.Application
import com.register.*
import com.register.di.Module.AppModule
import com.register.di.Module.DbModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DbModule::class])
interface AppComponent {

    fun inject(application: MyApplication)

    fun inject(registerFragment: RegisterFragment)

    fun inject(activity: MainActivity)

    fun inject(loginFragment: LoginFragment)

    fun inject(homeScreen: HomeScreen)

    fun inject(pinFragment: PinFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun dbModule(dbModule: DbModule): Builder

        fun build(): AppComponent
    }
}