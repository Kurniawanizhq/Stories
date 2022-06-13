package com.eone.submission1_bpai.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.data.model.common.Story
import com.eone.submission1_bpai.databinding.ActivityMainBinding
import com.eone.submission1_bpai.databinding.ListStoryBinding
import com.eone.submission1_bpai.remote.Resource
import com.eone.submission1_bpai.ui.addstory.AddStoryActivity
import com.eone.submission1_bpai.ui.auth.LoginActivity
import com.eone.submission1_bpai.utils.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnStoryItemClick {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getStories()
        }
    }

    private fun setupView() {
        binding.apply {

            swipe.setOnRefreshListener {
                lifecycleScope.launch {
                    viewModel.getStories()
                }
            }
            rvStory.setHasFixedSize(true)
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = StoryAdapter(this@MainActivity)

            ivAddStory.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }

            ivLogout.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
        }
    }

    private fun setupViewModel() {
        val pref = SessionPreference.getInstance(dataStore)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, application))[MainViewModel::class.java]

        lifecycleScope.launch {
            viewModel.getStories()
        }

        viewModel.stories.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        showLoading(false)
                        adapter.setData(it.data)
                        binding.rvStory.adapter = adapter

                        binding.swipe.isRefreshing = false
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onClick(story: Story, itemBinding : ListStoryBinding) {
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(itemBinding.imgStory, "imageDetail"),
                Pair(itemBinding.tvDesc, "nameDetail"),
                Pair(itemBinding.tvDesc, "description"),
                Pair(itemBinding.tvCreatedDate, "dateDetail"),
            )
        val intent = Intent(this, DetailActivity::class.java).putExtra(DetailActivity.DETAIL_EXTRA,story)
        startActivity(intent,optionsCompat.toBundle())
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}