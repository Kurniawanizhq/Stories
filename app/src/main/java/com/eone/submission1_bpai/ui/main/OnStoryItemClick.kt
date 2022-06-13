package com.eone.submission1_bpai.ui.main

import com.eone.submission1_bpai.data.model.common.Story
import com.eone.submission1_bpai.databinding.ListStoryBinding

interface OnStoryItemClick {
    fun onClick( story: Story, itemBinding : ListStoryBinding)
}