package com.sample.data.repo

import com.sample.data.mappers.ImageDetailsMapper
import com.sample.data.mappers.SearchResultMapper
import com.sample.data.apiservice.ApiService
import com.sample.domain.models.SearchResultModel
import com.sample.domain.repositories.ImageRemoteRepository
import io.reactivex.Single
import javax.inject.Inject

class ImageRemoteRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val imageDetailsMapper: dagger.Lazy<ImageDetailsMapper>,
    private val searchResultMapper: dagger.Lazy<SearchResultMapper>
) : ImageRemoteRepository {
    override fun getImages(queryString: String): Single<SearchResultModel> {
        return apiService.searchImages(queryString)
            .map {
                searchResultMapper.get().toSearchResult(imageDetailsMapper.get(), it)
            }
    }
}