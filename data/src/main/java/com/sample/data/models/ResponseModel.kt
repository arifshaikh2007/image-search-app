package com.sample.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseModel (
    @SerializedName("data")
    @Expose
    val dataList: List<SearchResultDataModel>,
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("status")
    @Expose
    val status: Int)