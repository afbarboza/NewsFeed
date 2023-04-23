package com.newsapi.newsfeed.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.newsapi.newsfeed.R
import com.newsapi.newsfeed.databinding.ActivityDetailsBinding
import com.newsapi.newsfeed.helpers.Helper.Companion.ARTICLE_PARAM
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.viewmodel.HeadlineDetailsViewModel
import com.newsapi.newsfeed.viewmodel.HeadlineDetailsViewModelFactory

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var headlineDetailsViewModel: HeadlineDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = getArticle()
        initViewModel(article)
        initObservers()
    }

    private fun getArticle(): Article? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(ARTICLE_PARAM, Article::class.java)
        } else {
            intent.extras?.getParcelable(ARTICLE_PARAM)
        }
    }

    private fun initViewModel(article: Article?) {
        if (article == null) {
            displayErrorState()
            return
        }

        val viewModelFactory = HeadlineDetailsViewModelFactory(article)
        headlineDetailsViewModel = ViewModelProvider(this, viewModelFactory)
            .get(HeadlineDetailsViewModel::class.java)

    }

    private fun initObservers() {
        headlineDetailsViewModel.articleData.observe(this) {
            binding.tvHeadlineDetailsTitle.text = it?.title
            binding.tvHeadlineDetailsDescription.text = it?.description ?: ""
            binding.tvHeadlineDetailsContent.text = it?.content ?: ""
            updateHeadlineImage(it)
        }
    }

    private fun displayErrorState() {
        Snackbar.make(binding.root, R.string.error_loading_data, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun updateHeadlineImage(article: Article?) {
        if (article?.urlToImage == null) {
            binding.ivHeadlineDetailsImage.visibility = View.GONE
        } else {
            loadUrlIntoImageView(article)
        }
    }

    private fun loadUrlIntoImageView(article: Article) {
        Glide.with(binding.root)
            .load(article.urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivHeadlineDetailsImage)
    }

}