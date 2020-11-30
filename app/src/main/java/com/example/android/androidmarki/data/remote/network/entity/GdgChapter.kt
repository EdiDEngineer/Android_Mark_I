package com.example.android.androidmarki.data.remote.network.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class GdgChapter(
    @Json(name = "chapter_name") val name: String,
    @Json(name = "cityarea") val city: String,
    val country: String,
    val region: String,
    val specialty: String,
    val website: String,
    val geo: LatLong
 ): Parcelable
@JsonClass(generateAdapter = true)
@Parcelize
data class LatLong(
    val lat: Double,
    @Json(name = "lng")
    val long: Double
) : Parcelable
@JsonClass(generateAdapter = true)
@Parcelize
data class GdgResponse(
        @Json(name = "filters_") val filters: Filter,
        @Json(name = "data") val chapters: List<GdgChapter>
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Filter(
        @Json(name = "region") val regions: List<String>
): Parcelable
