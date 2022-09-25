package com.eone.submission2_bpai.ui.addstory

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.eone.submission2_bpai.data.model.response.BaseResponse
import com.eone.submission2_bpai.remote.StoryRepository
import com.eone.submission2_bpai.remote.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class AddStoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getAuthToken(): Flow<String?> = authRepository.getAuthToken()

    suspend fun postStory(token :String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Result<BaseResponse>> = storyRepository.uploadImage(token, file, description, lat, lon)

}