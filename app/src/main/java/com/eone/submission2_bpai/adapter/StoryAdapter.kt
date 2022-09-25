package com.eone.submission2_bpai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eone.submission2_bpai.R
import com.eone.submission2_bpai.data.model.common.Story
import com.eone.submission2_bpai.databinding.ListStoryBinding
import com.eone.submission2_bpai.ui.main.OnStoryItemClick
import java.text.SimpleDateFormat
import java.util.*


class StoryAdapter(private val itemClickListener: OnStoryItemClick) :
    PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(DiffCallback) {

    inner class StoryViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(story: Story) {
            binding.apply {

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



                Glide.with(itemView.context)
                    .load(story.photoUrl)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(
//                            e: GlideException?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            Log.e("error :",e?.message.toString())
//                            progressBar.visibility = View.GONE
//                            return false
//                        }
//
//                        override fun onResourceReady(
//                            resource: Drawable?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            dataSource: DataSource?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            progressBar.visibility = View.GONE
//                            return false
//                        }
//                    })
//                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                    .error(R.drawable.img_placeholder)
                    .into(imgStory)

                itemView.setOnClickListener {
                    itemClickListener.onClick(story, binding)
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
        val story = getItem(position)
        if (story != null) {
            holder.binding(story)
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}