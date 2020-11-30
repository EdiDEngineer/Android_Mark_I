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
package com.example.android.androidmarki.ui.home.todo.taskdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.androidmarki.AndroidMarkI
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTodoTaskDetailBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.example.android.androidmarki.ui.home.todo.tasks.DELETE_RESULT_OK
import com.example.android.androidmarki.util.EventObserver
import com.example.android.androidmarki.util.setupRefreshLayout
import com.example.android.androidmarki.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar

/**
 * Main UI for the task detail screen.
 */
class TaskDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentTodoTaskDetailBinding

    private val args: TaskDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<TaskDetailViewModel> {
        BaseViewModelFactory(TaskDetailViewModel((requireContext().applicationContext as AndroidMarkI).taskRepository))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTodoTaskDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
navController = findNavController()
        viewModel.start(args.taskId)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFab()
        view.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        setupNavigation()
        setupRefreshLayout(binding.refreshLayout)
    }

    private fun setupNavigation() {
        viewModel.deleteTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TaskDetailFragmentDirections
                .actionTaskDetailFragmentToTasksFragment(DELETE_RESULT_OK)
         navController.navigate(action)
        })
        viewModel.editTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TaskDetailFragmentDirections
                .actionTaskDetailFragmentToAddEditTaskFragment(
                    args.taskId,
                    resources.getString(R.string.edit_task)
                )
          navController.navigate(action)
        })
    }

    private fun setupFab() {
        activity?.findViewById<View>(R.id.edit_task_fab)?.setOnClickListener {
            viewModel.editTask()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteTask()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu)
    }
}
