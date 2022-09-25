package com.eone.submission2_bpai.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eone.submission2_bpai.data.model.common.Story
import com.eone.submission2_bpai.data.model.response.BaseResponse
import com.eone.submission2_bpai.data.model.response.StoryResponse
import com.eone.submission2_bpai.local.StoryDatabase
import com.eone.submission2_bpai.local.remote.StoryRemoteMediator
import com.eone.submission2_bpai.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {

    fun getAllStories(token: String): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                apiService,
                generateBearerToken(token)
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAll()
            }
        ).flow
    }

    fun getAllStoriesWithLocation(token: String): Flow<Result<StoryResponse>> = flow {
            try {
                val bearerToken = generateBearerToken(token)
                val response = apiService.getStories(bearerToken, size = 30,  location = 1)
                emit(Result.success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.failure(e))
            }
    }

    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<Result<BaseResponse>> = flow {
        try {
            val bearerToken = generateBearerToken(token)
            val response = apiService.postStory(bearerToken, file, description, lat, lon)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }
}