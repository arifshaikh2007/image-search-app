package com.sample.imagesearch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.imagesearch.viewmodel.ImageDetailsViewModel
import com.sample.imagesearch.viewmodel.SearchViewModel
import com.sample.imagesearch.viewmodel.SelectedImageDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindMainViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageDetailsViewModel::class)
    abstract fun bindImageDetailsViewModel(viewModel: ImageDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectedImageDetailsViewModel::class)
    abstract fun bindSelectedImageDetailsViewModel(viewModel: SelectedImageDetailsViewModel): ViewModel

}