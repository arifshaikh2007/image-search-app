package com.sample.data.apiservice

import com.sample.data.models.ResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api service class to make gallery search request to imgur backend
 */
interface ApiService {
    @GET("3/gallery/search/1")
    fun searchImages(@Query("q") queryString: String): Single<ResponseModel>
}