package com.eone.submission1_bpai.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.data.model.common.Story
import com.eone.submission1_bpai.data.model.response.BaseResponse
import com.eone.submission1_bpai.data.model.response.StoryResponse
import com.eone.submission1_bpai.local.StoryDao
import com.eone.submission1_bpai.local.StoryDatabase
import com.eone.submission1_bpai.network.ApiConfig
import com.eone.submission1_bpai.remote.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SessionPreference, application: Application) :
    AndroidViewModel(application) {
    private var storyDao: StoryDao? = null
    private var storyDB: StoryDatabase? = StoryDatabase.getDatabase(application)
    private val _stories = MutableLiveData<Resource<ArrayList<Story>>>()
    val stories: LiveData<Resource<ArrayList<Story>>> = _stories

    init {
        storyDao = storyDB?.storyDao()
    }

    suspend fun getStories() {
        _stories.postValue(Resource.Loading())
        val client =
            ApiConfig.apiInstance.getStories(token =  "Bearer ${pref.getRealtimeToken().first()}")

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {
                        val listStory = it.listStory

                        viewModelScope.launch {
                            storyDao?.deleteAll()
                            listStory.forEach { story ->
                                storyDao?.insert(story)
                            }
                        }
                        _stories.postValue(Resource.Success(ArrayList(listStory)))
                    }
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _stories.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(
                    MainViewModel::class.java.simpleName,
                    "onFailure getStories"
                )
                _stories.postValue(Resource.Error(t.message))
            }
        })
    }



}