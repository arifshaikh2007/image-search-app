package com.sample.imagesearch.di

import android.app.Application
import com.sample.data.di.*
import com.sample.imagesearch.ImageSearchApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        ApiModule::class,
        AppModule::class,
        RemoteRepositoryModule::class,
        ImageDetailsRepositoryModule::class,
        AppDatabaseModule::class,
        CommentsDataModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class])
interface AppComponent : AndroidInjector<ImageSearchApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}