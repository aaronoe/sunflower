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

package com.google.samples.apps.sunflower.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.samples.apps.sunflower.api.github.GitHubRepository
import com.google.samples.apps.sunflower.api.github.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class RepositoriesViewModel @ViewModelInject constructor(
        private val repo: GitHubRepository
) : ViewModel() {

    private val _reposLiveData by lazy {
        liveData {
            delay(2500)
            emit(repo.getPage("JakeWharton", 1))
        }
    }

    private var pagingData: Flow<PagingData<Repository>>? = null

    fun getRepositoryFlow(userLogin: String): Flow<PagingData<Repository>> {
        pagingData?.let {
            return it
        }

        return synchronized(this) {
            if (pagingData == null) {
                val flow = repo.getRepositoriesSteam(userLogin)
                        .cachedIn(viewModelScope)

                pagingData = flow
            }

            requireNotNull(pagingData)
        }
    }

    fun getNextPage(): LiveData<List<Repository>> {
        return _reposLiveData
    }

}