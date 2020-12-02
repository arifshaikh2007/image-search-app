package com.sample.domain.usecases

import com.sample.domain.models.SearchResultModel
import com.sample.domain.repositories.ImageRemoteRepository
import io.reactivex.Single
import javax.inject.Inject

class SeachImagesUseCase @Inject constructor(private val repo: ImageRemoteRepository): SingleUseCase<SearchResultModel> {
    override fun execute(queryString: String): Single<SearchResultModel> {
        return repo.getImages(queryString)
    }
}