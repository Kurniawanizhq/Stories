package com.eone.submission2_bpai.ui.detail

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.eone.submission2_bpai.R
import com.eone.submission2_bpai.data.model.common.Story
import com.eone.submission2_bpai.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val itemStory = intent.getParcelableExtra<Story>(DETAIL_EXTRA)

        if (itemStory != null) {
            setupView(itemStory)
        }
    }

    private fun setupView(itemStory : Story){
        binding.apply {

            Glide.with(this@DetailActivity)
                .load(itemStory.photoUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }
                })
                .error(R.drawable.img_placeholder)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                .into(imgStory)

            tvUser.text = getString(R.string.detail_text_title,itemStory.name)
            tvName.text = itemStory.name
            tvDesc.text = itemStory.description
            tvCreatedDate.text = itemStory.createdAt?.let { it1 -> SimpleDateFormat(root.context.getString(R.string.isoFormat), Locale.getDefault()).parse(it1)?.let { it2 -> SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(it2) }
            }
        }
    }

    companion object {
        const val DETAIL_EXTRA = "1"
    }
}