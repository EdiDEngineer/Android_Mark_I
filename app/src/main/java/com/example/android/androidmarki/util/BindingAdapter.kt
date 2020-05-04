package com.example.android.androidmarki.util

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
import com.example.android.androidmarki.data.remote.network.entity.GdgChapter
import com.example.android.androidmarki.data.remote.network.entity.MarsProperty
import com.example.android.androidmarki.ui.home.gdgFinder.GdgListAdapter
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
@BindingAdapter("listMarsPropertyData")
fun RecyclerView.bindMarsRealEstateRecyclerView(data: List<MarsProperty>?) {
    val adapter = adapter as MarsRealEstatePhotoGridAdapter
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
/**
 * Binding adapter used to display images from URL using Glide
 */

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

/**
 * This binding adapter displays the [MarsApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("marsApiStatus")
fun ImageView.bindStatus(status: MarsApiStatus?) {
    when (status) {
        MarsApiStatus.LOADING -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.loading_animation)
        }
        MarsApiStatus.ERROR -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_connection_error)
        }
        MarsApiStatus.DONE -> {
            visibility = View.GONE
        }
    }
}


/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun View.goneIfNotNull(it: Any?) {
    visibility = if (it != null) View.GONE else View.VISIBLE
}


/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listGdgChapterData")
fun RecyclerView.bindRecyclerView(data: List<GdgChapter>?) {
    val adapter = adapter as GdgListAdapter
    adapter.submitList(data) {
        // scroll the list to the top after the diffs are calculated and posted
        scrollToPosition(0)
    }
}

@BindingAdapter("showOnlyWhenEmpty")
fun View.showOnlyWhenEmpty(data: List<Any>?) {
    visibility = when {
        data == null || data.isEmpty() -> View.VISIBLE
        else -> View.GONE
    }
}