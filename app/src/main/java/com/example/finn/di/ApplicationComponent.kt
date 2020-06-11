package com.app.nasa.DI

import android.app.Application
import com.app.nasatask.DI.Modules.ActivityBindingModule
import com.app.nasatask.DI.Modules.ContextModule
import com.app.nasatask.DI.Network.NetworkModule
import com.app.nasatask.DI.VMFactory.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,AndroidSupportInjectionModule::class,
    ActivityBindingModule::class, NetworkModule::class,
    ViewModelFactoryModule::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

}