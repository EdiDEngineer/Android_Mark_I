/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.androidmarki.ui.home.todo.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.android.androidmarki.AndroidMarkI
import com.example.android.androidmarki.databinding.FragmentTodoStatisticsBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.example.android.androidmarki.util.setupRefreshLayout

/**
 * Main UI for the statistics screen.
 */
class StatisticsFragment : BaseFragment() {

    private lateinit var binding: FragmentTodoStatisticsBinding
    private val viewModel by viewModels<StatisticsViewModel> {
        BaseViewModelFactory(

           StatisticsViewModel( (requireContext().applicationContext as AndroidMarkI).taskRepository,
               AndroidMarkI.getApp()
           )
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoStatisticsBinding.inflate(inflater,container, false).apply{
            viewmodel =viewModel
            lifecycleOwner = viewLifecycleOwner
            setupRefreshLayout(refreshLayout)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
