package com.sample.data.models

import com.google.gson.annotations.SerializedName

data class SearchResultDataModel(
    @SerializedName("images")
    val images: List<ImageDataModel>,
    @SerializedName("title")
    val title: String)