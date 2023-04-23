package com.newsapi.newsfeed.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.newsapi.newsfeed.R
import com.newsapi.newsfeed.databinding.ArticleViewHolderBinding
import com.newsapi.newsfeed.helpers.Helper
import com.newsapi.newsfeed.helpers.Helper.Companion.ARTICLE_PARAM
import com.newsapi.newsfeed.model.Article

class ArticleViewHolder(
    private val binding: ArticleViewHolderBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article) {
        setupContent(article)
        setPublishedAt(article.publishedAt)
        updateHeadlineImage(article)
        setupOnClickListener(article)
    }

    private fun setupContent(article: Article) {
        binding.tvHeadlineTitle.text = article.title
        binding.tvHeadlineDescription.text = article.description ?: ""
    }

    private fun setPublishedAt(dateStr: String) {
        val dateStr = Helper.formatDate(dateStr)
        if (dateStr.isNullOrEmpty()) {
            binding.tvHeadlinePublishedAt.visibility = View.INVISIBLE
        } else {
            setupTvHeadlinePublishedAt(dateStr)
        }
    }

    private fun setupTvHeadlinePublishedAt(dateStr: String) {
        val publishedOn = binding.root.context.getString(
            R.string.published_on
        )
        binding.tvHeadlinePublishedAt.text = "$publishedOn $dateStr"
    }

    private fun updateHeadlineImage(article: Article) {
        if (article.urlToImage == null) {
            binding.ivHeadlineImage.visibility = View.INVISIBLE
        } else {
            loadUrlIntoImageView(article)
        }
    }

    private fun loadUrlIntoImageView(article: Article) {
        Glide.with(binding.root)
            .load(article.urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivHeadlineImage)
    }

    private fun setupOnClickListener(article: Article) {
        binding.clHeadlinerContainer.setOnClickListener {
            navigateToDetailsActivity(article)
        }
    }

    private fun navigateToDetailsActivity(article: Article) {
        val context = binding.clHeadlinerContainer.context
        val intent = Intent(context, DetailsActivity::class.java)
        val extras = Bundle()
        extras.putParcelable(ARTICLE_PARAM, article)
        intent.putExtras(extras)
        context.startActivity(intent)
    }

}