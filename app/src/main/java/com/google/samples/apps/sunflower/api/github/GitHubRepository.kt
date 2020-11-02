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

package com.google.samples.apps.sunflower.api.github

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class GitHubRepository @Inject constructor(
        private val service: GitHubService
) {

    fun getRepositoriesSteam(userLogin: String): Flow<PagingData<Repository>> {
        return Pager(
                config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = { GitHubPagingSource(service, userLogin) }
        ).flow
    }

    suspend fun getPage(userLogin: String, page: Int): List<Repository> {
        return service.getReposForUser(userLogin, NETWORK_PAGE_SIZE, page)
    }

    suspend fun getRepositoryDetails(repoUser: String, repoName: String): Repository {
        return service.getRepoDetails(repoUser, repoName)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }

}