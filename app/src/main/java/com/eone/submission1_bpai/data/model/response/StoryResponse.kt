package com.eone.submission1_bpai.data.model.response

import com.eone.submission1_bpai.data.model.common.Story
import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("listStory")
    val listStory: List<Story>
)
