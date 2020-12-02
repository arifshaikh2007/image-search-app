package com.sample.imagesearch.di

import android.app.Application
import android.content.Context
import com.sample.imagesearch.rx.SchedulersFacade
import com.sample.imagesearch.rx.SchedulersProvider
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun providerScheduler(schedulersFacade: SchedulersFacade): SchedulersProvider
}