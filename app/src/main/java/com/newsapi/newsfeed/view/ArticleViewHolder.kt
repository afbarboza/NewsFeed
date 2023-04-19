package com.newsapi.newsfeed.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.newsapi.newsfeed.R
import com.newsapi.newsfeed.databinding.ArticleViewHolderBinding
import com.newsapi.newsfeed.helpers.Helper
import com.newsapi.newsfeed.model.Article

class ArticleViewHolder(
    private val binding: ArticleViewHolderBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article) {
        binding.tvHeadlineTitle.text = article.title
        binding.tvHeadlineDescription.text = article.title
        setupPublishedAt(article.publishedAt)

        Glide.with(binding.root)
            .load(article.urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivHeadlineImage)
    }

    private fun setupPublishedAt(dateStr: String) {
        val dateStr = Helper.formatDate(dateStr)
        if (dateStr.isNullOrEmpty()) {
            binding.tvHeadlinePublishedAt.visibility = View.INVISIBLE
            return
        }

        val publishedOn = binding.root.context.getString(
            R.string.published_on
        )

        binding.tvHeadlinePublishedAt.text = "$publishedOn $dateStr"
    }
}