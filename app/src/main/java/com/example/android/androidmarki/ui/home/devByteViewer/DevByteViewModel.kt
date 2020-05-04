/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.androidmarki.ui.home.devByteViewer


import androidx.lifecycle.viewModelScope
import com.example.android.androidmarki.data.local.AndroidMarkIDatabase.Companion.getDatabaseInstance
import com.example.android.androidmarki.data.repository.VideosRepository
import com.example.android.androidmarki.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * DevByteViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class DevByteViewModel : BaseViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */

    private val database = getDatabaseInstance(getApplication())
    private val videosRepository = VideosRepository(database)

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        viewModelScope.launch {
            try {
                videosRepository.refreshVideos()
            } catch (e: Exception) {
            }
        }
    }

    val playlist = videosRepository.videos

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */


}
