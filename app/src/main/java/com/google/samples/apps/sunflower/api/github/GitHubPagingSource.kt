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

import androidx.paging.PagingSource

class GitHubPagingSource(
        private val service: GitHubService,
        private val userLogin: String,
) : PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val page = params.key ?: FIRST_PAGE_INDEX

        return try {
            val response = service.getReposForUser(userLogin, params.loadSize, page)

            LoadResult.Page(
                    data = response,
                    prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                    // TODO: figure out how many pages there are
                    nextKey = if (page > 15) null else page + 1
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

}