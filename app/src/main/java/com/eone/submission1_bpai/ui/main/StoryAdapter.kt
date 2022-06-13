package com.eone.submission1_bpai.ui.main

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.eone.submission1_bpai.R
import com.eone.submission1_bpai.data.model.common.Story
import com.eone.submission1_bpai.databinding.ListStoryBinding
import java.text.SimpleDateFormat
import java.util.*

class StoryAdapter internal constructor(
    private val itemClickListener: OnStoryItemClick
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private var stories: ArrayList<Story> = arrayListOf()

    fun setData(stories: ArrayList<Story>) {
        this.stories.clear()
        this.stories = stories
    }

    inner class StoryViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(position: Int) {
            binding.apply {

                val story = stories[position]
                imgStory.transitionName = "${story.id}image"
                tvName.transitionName = "${story.id}name"
                tvDesc.transitionName = "${story.id}desc"
                tvCreatedDate.transitionName = "${story.id}date"
                tvName.text = story.name
                tvDesc.text = story.description

                if (story.description?.length!! > 100) {
                    tvMore.visibility = View.VISIBLE
                    tvDesc.isSingleLine = false
                } else {
                    tvMore.visibility = View.GONE
                }

                tvCreatedDate.text = story.createdAt?.let { it1 ->
                    SimpleDateFormat(
                        root.context.getString(R.string.isoFormat),
                        Locale.getDefault()
                    ).parse(
                        it1
                    )?.let { it2 ->
                        SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(it2)
                    }
                }

                progressBar.visibility = View.VISIBLE

                Glide.with(itemView)
                    .load(stories[position].photoUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                    .into(imgStory)

                itemView.setOnClickListener {
                    itemClickListener.onClick(story,binding)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            ListStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.binding(position)
    }

    override fun getItemCount(): Int {
        return stories.size
    }
}