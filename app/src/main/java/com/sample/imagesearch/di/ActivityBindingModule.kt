package com.sample.imagesearch.di

import com.sample.imagesearch.ui.ImageDetailsFragment
import com.sample.imagesearch.ui.MainActivity
import com.sample.imagesearch.ui.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}

@Module(includes = [ViewModelModule::class])
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun searchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun imageDetailsFragment(): ImageDetailsFragment

}