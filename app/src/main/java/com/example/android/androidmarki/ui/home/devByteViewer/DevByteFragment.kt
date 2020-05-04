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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.domain.model.Video
import com.example.android.androidmarki.databinding.DevbyteItemBinding
import com.example.android.androidmarki.databinding.FragmentDevByteBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory


/**
 * Show a list of DevBytes on screen.
 */
class DevByteFragment : BaseFragment() {




    private lateinit var binding: FragmentDevByteBinding

    /**
     * RecyclerView Adapter for converting a list of Video to cards.
     */
    private lateinit var viewModelAdapter: DevByteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel!!.playlist.observe(viewLifecycleOwner, Observer { videos ->
            videos?.apply {
                viewModelAdapter.videos = videos
            }
        })
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDevByteBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            // Set the lifecycleOwner so DataBinding can observe LiveData

            lifecycleOwner = viewLifecycleOwner
            /**
             * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
             * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
             * do in this Fragment.
             */
        viewModel =  ViewModelProvider(this@DevByteFragment, BaseViewModelFactory(DevByteViewModel()))
            .get(DevByteViewModel::class.java)
        }

        viewModelAdapter = DevByteAdapter(DevByteAdapter.VideoClick {
            // When a video is clicked this block or lambda will be called by DevByteAdapter

            // context is not around, we can safely discard this click since the Fragment is no
            // longer on the screen
            val packageManager = context?.packageManager ?: return@VideoClick

            // Try to generate a direct intent to the YouTube app
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if (intent.resolveActivity(packageManager) == null) {
                // YouTube app isn't found, use the web url
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

    /**
     * Helper method to generate YouTube app links
     */
    private val Video.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}

