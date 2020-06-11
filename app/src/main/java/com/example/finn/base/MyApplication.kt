package com.app.nasatask.Base


import com.app.nasa.DI.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class MyApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {


        val component = DaggerApplicationComponent.builder().application(this)
            .build()
        component.inject( this)
         // add database to component builder

        return component
    }


}
