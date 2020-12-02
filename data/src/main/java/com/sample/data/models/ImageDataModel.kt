package com.sample.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ImageDataModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("link")
    var url: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("title")
    var title: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(url)
        parcel.writeString(type)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageDataModel> {
        override fun createFromParcel(parcel: Parcel): ImageDataModel {
            return ImageDataModel(parcel)
        }

        override fun newArray(size: Int): Array<ImageDataModel?> {
            return arrayOfNulls(size)
        }
    }
}