package com.newsapi.newsfeed.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newsapi.newsfeed.Injection
import com.newsapi.newsfeed.databinding.ActivityMainBinding
import com.newsapi.newsfeed.viewmodel.TopHeadlinesPageViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding:  ActivityMainBinding
    private lateinit var topHeadlinesPageViewModel: TopHeadlinesPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel by viewModels<TopHeadlinesPageViewModel> (
            factoryProducer = {Injection.provideTopHeadlinesPageViewModelFactory(this)}
        )
        this.topHeadlinesPageViewModel = viewModel


        val listItems = viewModel.items
        val articleAdapter = ArticleAdapter()
        initRecyclerView(articleAdapter)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                listItems.observe(this@MainActivity, {
                    articleAdapter.submitData(lifecycle, it)
                })
            }
        }
    }

    private fun initRecyclerView(adapter: ArticleAdapter) {
        binding.rvNewsList.adapter = adapter
        binding.rvNewsList.layoutManager = LinearLayoutManager(binding.rvNewsList.context)
        val decoration = DividerItemDecoration(binding.rvNewsList.context, DividerItemDecoration.VERTICAL)
        binding.rvNewsList.addItemDecoration(decoration)
    }
}