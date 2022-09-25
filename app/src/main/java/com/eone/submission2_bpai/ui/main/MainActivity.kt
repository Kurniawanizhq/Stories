package com.eone.submission2_bpai.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.eone.submission2_bpai.adapter.LoadingStateAdapter
import com.eone.submission2_bpai.adapter.StoryAdapter
import com.eone.submission2_bpai.data.model.common.Story
import com.eone.submission2_bpai.databinding.ActivityMainBinding
import com.eone.submission2_bpai.databinding.ListStoryBinding
import com.eone.submission2_bpai.ui.addstory.AddStoryActivity
import com.eone.submission2_bpai.ui.detail.DetailActivity
import com.eone.submission2_bpai.ui.location.LocationActivity
import com.eone.submission2_bpai.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class MainActivity : AppCompatActivity(), OnStoryItemClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private val viewModel: MainViewModel by viewModels()
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(EXTRA_TOKEN) ?: ""

        setSwipeRefreshLayout()
        setupViewModel()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        getAllStories()
    }

    private fun setupView() {
        binding.apply {
            storyAdapter = StoryAdapter(this@MainActivity)

            storyAdapter.addLoadStateListener { loadState ->
                if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && storyAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                    binding.apply {
                        rvStory.visibility = View.GONE
                    }
                } else {
                    binding.apply {
                        rvStory.visibility = View.VISIBLE
                    }
                }
            }

            try {
                rvStory.apply {
                    adapter = storyAdapter.withLoadStateFooter(
                        footer = LoadingStateAdapter {
                            storyAdapter.retry()
                        }
                    )

                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }


            ivAddStory.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }

            ivLocation.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity, LocationActivity::class.java).putExtra(
                        EXTRA_TOKEN, token
                    )
                )
            }

            ivLogout.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
        }
    }

    private fun setupViewModel() {
        getAllStories()
    }

    private fun getAllStories() {
        viewModel.getAllStories(token).observe(this) {
            updateRecyclerViewData(it)
        }
    }

    private fun setSwipeRefreshLayout() {
        binding.swipe.setOnRefreshListener {
            getAllStories()
            binding.swipe.isRefreshing = false
        }
    }

    override fun onClick(story: Story, itemBinding: ListStoryBinding) {
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(itemBinding.imgStory, "imageDetail"),
                Pair(itemBinding.tvDesc, "nameDetail"),
                Pair(itemBinding.tvDesc, "description"),
                Pair(itemBinding.tvCreatedDate, "dateDetail"),
            )
        val intent =
            Intent(this, DetailActivity::class.java).putExtra(DetailActivity.DETAIL_EXTRA, story)
        startActivity(intent, optionsCompat.toBundle())
    }

    private fun updateRecyclerViewData(stories: PagingData<Story>) {
        val recyclerViewState = binding.rvStory.layoutManager?.onSaveInstanceState()
        storyAdapter.submitData(lifecycle, stories)
        binding.rvStory.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}