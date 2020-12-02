package com.sample.imagesearch

import com.sample.imagesearch.di.AppComponent
import com.sample.imagesearch.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ImageSearchApplication : DaggerApplication() {
    private lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
        return appComponent
    }
}