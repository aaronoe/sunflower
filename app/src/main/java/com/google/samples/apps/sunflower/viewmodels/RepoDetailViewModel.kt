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
import androidx.lifecycle.*
import com.google.samples.apps.sunflower.api.github.GitHubRepository
import com.google.samples.apps.sunflower.api.github.Repository
import com.google.samples.apps.sunflower.utilities.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.plus

class RepoDetailViewModel @ViewModelInject constructor(
        private val repo: GitHubRepository
): ViewModel() {

    private val repoId = MutableLiveData<Pair<String, String>>()

    val repoDetailData = repoId.switchMap { (user, repoName) ->
        liveData<Resource<Repository>> {
            emit(Resource.Loading())

            runCatching {
                repo.getRepositoryDetails(user, repoName)
            }.onSuccess {
                emit(Resource.Success(it))
            }.onFailure {
                emit(Resource.Failure(it))
            }
        }
    }

    val result = repoDetailData.asFlow()
            .filterIsInstance<Resource.Success<Repository>>()
            .map { it.data }
            .asLiveData()

    fun fetchData(user: String, repo: String) {
        repoId.postValue(user to repo)
    }

}