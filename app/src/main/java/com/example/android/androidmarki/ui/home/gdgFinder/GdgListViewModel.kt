package com.example.android.androidmarki.ui.home.gdgFinder

import android.location.Location
import androidx.lifecycle.*
import com.example.android.androidmarki.data.remote.network.entity.GdgChapter
import com.example.android.androidmarki.data.remote.network.service.GdgApi
import com.example.android.androidmarki.data.repository.GdgChapterRepository
import com.example.android.androidmarki.ui.base.BaseViewModel
import kotlinx.coroutines.*
import java.io.IOException


class GdgListViewModel:BaseViewModel() {

    private val repository = GdgChapterRepository(GdgApi.retrofitService)

    private var filter = FilterHolder()

    private var currentJob: Job? = null

    private val _gdgList = MutableLiveData<List<GdgChapter>>()
    private val _regionList = MutableLiveData<List<String>>()
    private val _showNeedLocation = MutableLiveData<Boolean>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val gdgList: LiveData< List<GdgChapter>>
        get() = _gdgList

    val regionList: LiveData<List<String>>
        get() = _regionList

    val showNeedLocation: LiveData<Boolean>
        get() = _showNeedLocation

    init {
        // process the initial filter
        onQueryChanged()

        viewModelScope.launch {
            delay(5_000)
            _showNeedLocation.postValue( !repository.isFullyInitialized)
        }
    }

    private fun onQueryChanged() {
        currentJob?.cancel() // if a previous query is running cancel it before starting another
        currentJob = viewModelScope.launch {
            try {
                // this will run on a thread managed by Retrofit
                _gdgList.postValue( repository.getChaptersForFilter(filter.currentValue))
                repository.getFilters().let {
                    // only update the filters list if it's changed since the last time
                    if (it != _regionList.value) {
                        _regionList.postValue(it)
                    }
                }
            } catch (e: IOException) {
                _gdgList.postValue( listOf())
            }catch (e: Exception) {
            }
        }
    }

    fun onLocationUpdated(location: Location) {
        viewModelScope.launch {
      try {
                repository.onLocationChanged(location)
            }catch (e:Exception){

            }
            onQueryChanged()
        }

    }

    fun onFilterChanged(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {
            onQueryChanged()
        }
    }

    private class FilterHolder {
        var currentValue: String? = null
            private set

        fun update(changedFilter: String, isChecked: Boolean): Boolean {
            if (isChecked) {
                currentValue = changedFilter
                return true
            } else if (currentValue == changedFilter) {
                currentValue = null
                return true
            }
            return false
        }
    }
}

