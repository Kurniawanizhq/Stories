package com.eone.submission1_bpai.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.eone.submission1_bpai.R
import com.eone.submission1_bpai.data.model.common.Story
import com.eone.submission1_bpai.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemStory = intent.getParcelableExtra<Story>(DETAIL_EXTRA)

        if (itemStory != null) {
            setupView(itemStory)
        }
    }

    private fun setupView(itemStory : Story){
        binding.apply {

            Glide.with(this@DetailActivity)
                .load(itemStory.photoUrl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                .into(imgStory)

            tvName.text = itemStory.name
            tvDesc.text = itemStory.description
            tvCreatedDate.text = itemStory.createdAt?.let { it1 ->
                SimpleDateFormat(
                    root.context.getString(R.string.isoFormat),
                    Locale.getDefault()
                ).parse(
                    it1
                )?.let { it2 ->
                    SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(it2)
                }
            }
        }
    }

    companion object {
        const val DETAIL_EXTRA = "1"
    }
}