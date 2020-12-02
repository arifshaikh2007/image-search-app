package com.sample.data.mappers

import com.sample.data.models.ResponseModel
import com.sample.domain.models.SearchError
import com.sample.domain.models.SearchResultModel
import dagger.Lazy
import javax.inject.Inject

/**
 * This class provides apis to convert server response to {@link SearchResultModel}
 */
class SearchResultMapper @Inject constructor() : Lazy<SearchResultMapper> {
    /**
     * Returns {@link SearchResultModel} for given {@link ResponseModel} using {@link ImageDetailsMapper}
     * This method filters any response with null images or of type other than images
     * @param imageDetailsMapper ImageDetailsMapper to map response to image details
     * @param responseDataModel response received from backend
     *
     * @return SearchResultModel returns {@link SearchResultModel}
     */
    fun toSearchResult(imageDetailsMapper: ImageDetailsMapper, responseDataModel: ResponseModel): SearchResultModel {
        return if (responseDataModel.success) {
            val images = responseDataModel.dataList.filter{ it.images!=null }.onEach { v ->
                run {
                    v.images.forEach { it.title = v.title }
                }
            }.flatMap { it.images }
                .filter { !it.type.isNullOrBlank() && it.type.startsWith("image") && !it.url.isNullOrBlank() && it.url!!.contains(".") }
                .map { imageDetailsMapper.toImageDetailsModel(it)}

            SearchResultModel.Success(images)
        } else {
            SearchResultModel.Failure(SearchError.ServerError(responseDataModel.status))
        }
    }

    override fun get(): SearchResultMapper {
        return this
    }
}