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

package com.example.android.androidmarki.ui.home.miniPaint

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.remote.network.service.MarsApiFilter
import com.example.android.androidmarki.databinding.FragmentMarsRealEstateOverviewBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class MiniPaintFragment : BaseFragment() {



    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val myCanvasView = context?.let { MyCanvasView(it) }
        // No XML file; just one custom view created programmatically.
        // Request the full available screen for layout.
        myCanvasView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        myCanvasView?.contentDescription = getString(R.string.canvasContentDescription)
        return myCanvasView
    }


}
