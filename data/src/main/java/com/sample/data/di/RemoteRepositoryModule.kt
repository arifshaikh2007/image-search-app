package com.sample.data.di

import com.sample.data.mappers.ImageDetailsMapper
import com.sample.data.mappers.SearchResultMapper
import com.sample.data.apiservice.ApiService
import com.sample.data.repo.ImageRemoteRepositoryImp
import com.sample.domain.repositories.ImageRemoteRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ApiModule::class, MapperModule::class])
class RemoteRepositoryModule {
    @Provides
    fun provideImageRemoteRepository(apiService: ApiService, imageDetailsMapper: ImageDetailsMapper, searchResultMapper: SearchResultMapper): ImageRemoteRepository {
        return ImageRemoteRepositoryImp(apiService, imageDetailsMapper, searchResultMapper)
    }
}