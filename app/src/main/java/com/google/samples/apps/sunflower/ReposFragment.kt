/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.samples.apps.sunflower.adapters.RepositoryAdapter
import com.google.samples.apps.sunflower.databinding.FragmentReposBinding
import com.google.samples.apps.sunflower.viewmodels.RepositoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReposFragment : Fragment() {

    private val viewModel: RepositoriesViewModel by viewModels()
    private val args by navArgs<ReposFragmentArgs>()
    private val adapter = RepositoryAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return FragmentReposBinding.inflate(inflater, container, false).run {
            this.repoList.adapter = adapter
            getReposForUser()

            root
        }
    }

    private fun getReposForUser() {
        lifecycleScope.launch {
            viewModel.getRepositoryFlow(args.userName).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}