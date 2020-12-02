package com.sample.data.mappers

import com.sample.data.models.ImageDataModel
import com.sample.domain.models.ImageDetailsModel
import dagger.Lazy
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * This is mapper class which provides API's to convert objects from {@link ImageDataModel}
 * to {@link ImageDetailsModel} and vice versa
 *
 */
class ImageDetailsMapper @Inject constructor() : Lazy<ImageDetailsMapper> {
    /**
     * Returns {@link ImageDetailsModel} for given {@link ImageDataModel}
     * @param imageModel ImageModel object to convert to ImageDetailModel
     *
     * @return ImageDetailsModel Returns {@link ImageDetailsModel}
     */
    fun toImageDetailsModel(imageModel: ImageDataModel): ImageDetailsModel {
        return ImageDetailsModel(
                imageModel.id!!,
                imageModel.url!!,
                getTumbnailImageUrl(imageModel.url!!),
                imageModel.title!!,
                imageModel.type!!
        )
    }

    /**
     * Returns {@link ImageDataModel} for given {@link ImageDetailsModel}
     * @param imageDetailsModel ImageDetailsModel object to convert to ImageDataModel
     *
     * @return ImageDataModel Returns {@link ImageDataModel}
     */
    fun toImageDataModel(imageDetailsModel: ImageDetailsModel): ImageDataModel {
        return ImageDataModel(
            imageDetailsModel.id,
            imageDetailsModel.url,
            imageDetailsModel.type,
            imageDetailsModel.title
            )
    }
    
    override fun get(): ImageDetailsMapper {
        return this
    }

    /**
     * Also this class contains private getTumbnailImageUrl() which returns thumbnail url for given
     * imgur image url
     */
    private fun getTumbnailImageUrl(actualImageUrl: String): String {
        return StringBuilder(actualImageUrl).insert(actualImageUrl.lastIndexOf('.'), 's').toString()
    }
}