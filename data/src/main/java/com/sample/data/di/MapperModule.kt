package com.sample.data.di

import com.sample.data.mappers.ImageDetailsMapper
import com.sample.data.mappers.SearchResultMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {
    @Provides
    @Singleton
    fun provideImageDetailsMapper(): ImageDetailsMapper {
        return ImageDetailsMapper()
    }

    @Provides
    @Singleton
    fun provideSearchResultMapper(): SearchResultMapper {
        return SearchResultMapper()
    }
}