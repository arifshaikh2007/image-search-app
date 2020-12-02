package com.sample.domain.models

data class ImageDetailsModel(val id: String,
                             var url: String,
                             var thumbnailUrl: String,
                             var title: String,
                             var type: String)