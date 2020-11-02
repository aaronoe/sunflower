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

package com.google.samples.apps.sunflower.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.samples.apps.sunflower.ReposFragmentDirections
import com.google.samples.apps.sunflower.api.github.Repository
import com.google.samples.apps.sunflower.databinding.ListItemRepositoryBinding

class RepositoryAdapter : PagingDataAdapter<Repository, RepositoryAdapter.RepoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
                ListItemRepositoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    class RepoViewHolder(
            private val binding: ListItemRepositoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val repoId = binding.repo?.name ?: return@setOnClickListener
                val repoUser = binding.repo?.owner?.login ?: return@setOnClickListener

                val directions = ReposFragmentDirections.actionRepoListToDetails(
                        repoId = repoId,
                        repoUser = repoUser
                )
                binding.root.findNavController().navigate(directions)
            }
        }

        fun bind(repository: Repository) {
            binding.repo = repository
            binding.executePendingBindings()
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }
    }
}