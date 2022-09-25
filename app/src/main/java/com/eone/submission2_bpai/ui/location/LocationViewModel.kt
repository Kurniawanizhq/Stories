package com.eone.submission2_bpai.ui.location

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.eone.submission2_bpai.data.model.response.StoryResponse
import com.eone.submission2_bpai.remote.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class LocationViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    fun getStories(token: String): Flow<Result<StoryResponse>> =
        storyRepository.getAllStoriesWithLocation(token)
}