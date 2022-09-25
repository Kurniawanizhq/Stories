package com.eone.submission2_bpai.ui.main

import com.eone.submission2_bpai.data.model.common.Story
import com.eone.submission2_bpai.databinding.ListStoryBinding

interface OnStoryItemClick {
    fun onClick(story: Story, itemBinding : ListStoryBinding)
}