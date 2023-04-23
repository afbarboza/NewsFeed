package com.newsapi.newsfeed.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.newsapi.newsfeed.Injection
import com.newsapi.newsfeed.R
import com.newsapi.newsfeed.databinding.ActivityMainBinding
import com.newsapi.newsfeed.viewmodel.TopHeadlinesPageViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding:  ActivityMainBinding
    private lateinit var topHeadlinesPageViewModel: TopHeadlinesPageViewModel

    private lateinit var articleAdapter: ArticleAdapter

    @VisibleForTesting
    lateinit var rvNewsList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        disableDarkMode()
        initViewModel()
        initRecyclerView()
        initErrorStateListener()
        initHeadlinesDataListener()
        initLoadingStateListener()
        initNewsProviderName()
        topHeadlinesPageViewModel.getHeadlinesPagingSource()
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initViewModel() {
        val viewModel by viewModels<TopHeadlinesPageViewModel> (
            factoryProducer = {Injection.provideTopHeadlinesPageViewModelFactory(this)}
        )
        this.topHeadlinesPageViewModel = viewModel
    }

    private fun initRecyclerView() {
        rvNewsList = binding.rvNewsList
        this.articleAdapter = ArticleAdapter()
        binding.rvNewsList.adapter = this.articleAdapter
        binding.rvNewsList.layoutManager = LinearLayoutManager(binding.rvNewsList.context)
    }

    private fun initErrorStateListener() {
        articleAdapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.append    is LoadState.Error ->   loadState.append as LoadState.Error
                loadState.prepend   is LoadState.Error ->   loadState.prepend as LoadState.Error
                loadState.refresh   is LoadState.Error ->   loadState.refresh as LoadState.Error
                else -> null
            }

            errorState?.let {
                displayErrorState()
            }
        }
    }

    private fun initHeadlinesDataListener() {
        val listItems = topHeadlinesPageViewModel.items
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                listItems.observe(this@MainActivity) {
                    articleAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun initLoadingStateListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                articleAdapter.loadStateFlow.collect {
                    binding.pbHeadlineLoad.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
    }

    private fun initNewsProviderName() {
        topHeadlinesPageViewModel.newsProviderName.observe(this) {
            binding.tvSourceName.text = it
        }
    }

    private fun displayErrorState() {
        Snackbar.make(binding.root, R.string.error_generic, Snackbar.LENGTH_LONG)
            .show()
    }

}