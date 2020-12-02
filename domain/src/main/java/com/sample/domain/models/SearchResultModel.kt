package com.sample.domain.models

sealed class SearchResultModel {
    data class Failure(val searchError: SearchError) : SearchResultModel()
    data class Success(val imageList: List<ImageDetailsModel>) : SearchResultModel()
}

sealed class SearchError {
    data class NetworkError(val errorCode:Int): SearchError()
    data class ServerError(val errorCode: Int): SearchError()
}