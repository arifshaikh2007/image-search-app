package com.sample.domain.repositories

import com.sample.domain.models.SearchResultModel
import io.reactivex.Single

interface ImageRemoteRepository {
    fun getImages(queryString: String): Single<SearchResultModel>
}