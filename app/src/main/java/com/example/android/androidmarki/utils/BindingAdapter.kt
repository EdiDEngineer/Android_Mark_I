package com.example.android.androidmarki.utils

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.local.entity.SleepTrackerNight
import com.example.android.androidmarki.data.remote.network.MarsProperty
import com.example.android.androidmarki.ui.home.marsRealEstate.overview.MarsApiStatus
import com.example.android.androidmarki.ui.home.marsRealEstate.overview.MarsRealEstatePhotoGridAdapter
import com.google.android.material.textfield.TextInputLayout

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */

@BindingAdapter("error")
fun TextInputLayout.error(error: Int?) {
    this.error = error?.let { context.getString(it) }
}

@BindingAdapter("done")
inline fun EditText.done(crossinline function: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_UNSPECIFIED -> {
                function()
            }
        }
        false
    }
}

@BindingAdapter("src")
fun ImageView.src(@DrawableRes drawable: Int) {
    setImageResource(drawable)
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepTrackerNight?) {
    item?.let {
        setImageResource(
            when (item.sleepQuality) {
                0 -> R.drawable.sleep_tracker_ic_sleep_0
                1 -> R.drawable.sleep_tracker_ic_sleep_1
                2 -> R.drawable.sleep_tracker_ic_sleep_2
                3 -> R.drawable.sleep_tracker_ic_sleep_3
                4 -> R.drawable.sleep_tracker_ic_sleep_4
                5 -> R.drawable.sleep_tracker_ic_sleep_5
                else -> R.drawable.sleep_tracker_ic_sleep_active
            }
        )
    }
}

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepTrackerNight?) {
    item?.let {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }
}

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepTrackerNight?) {
    item?.let {
        text = convertNumericQualityToString(item.sleepQuality, context.resources)
    }
}



/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsProperty>?) {
    val adapter = recyclerView.adapter as MarsRealEstatePhotoGridAdapter
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

/**
 * This binding adapter displays the [MarsApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("marsApiStatus")
fun bindStatus(statusImageView: ImageView, status: MarsApiStatus?) {
    when (status) {
        MarsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MarsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MarsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}


