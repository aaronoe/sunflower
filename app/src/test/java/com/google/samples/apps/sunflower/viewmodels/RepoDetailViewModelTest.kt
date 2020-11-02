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

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.samples.apps.sunflower.api.github.GitHubRepository
import com.google.samples.apps.sunflower.api.github.RepoUser
import com.google.samples.apps.sunflower.api.github.Repository
import com.google.samples.apps.sunflower.utilities.Resource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class RepoDetailViewModelTest {

    private val repository = Repository(
            12, "", "", 1, "kt", RepoUser("", "", 42)
    )

    private val repo = Mockito.mock(GitHubRepository::class.java)
    private lateinit var vm: RepoDetailViewModel

    @Before
    fun setUp() {
        vm = RepoDetailViewModel(repo)
    }

    @Test
    fun `repo loads successfully`() {
        Mockito.`when`(repo.getRepositoryDetails()).thenReturn(repository)
        val liveData = vm.repoDetailData

        val value = liveData.getOrAwaitValue(500, TimeUnit.MILLISECONDS)
        assertTrue(value is Resource.Loading)
    }

    fun <T> LiveData<T>.getOrAwaitValue(
            time: Long = 2,
            timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

}