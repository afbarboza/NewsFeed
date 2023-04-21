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
        setupPublishedAt(article.publishedAt)
        setupHeadlineImage(article)
        setupOnClickListener(article)
    }

    private fun setupContent(article: Article) {
        binding.tvHeadlineTitle.text = article.title
        binding.tvHeadlineDescription.text = article.description ?: ""
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

    private fun setupHeadlineImage(article: Article) {
        if (article.urlToImage == null) {
            binding.ivHeadlineImage.visibility = View.INVISIBLE
            return
        }

        Glide.with(binding.root)
            .load(article.urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivHeadlineImage)
    }

    private fun setupOnClickListener(article: Article) {
        binding.clHeadlinerContainer.setOnClickListener {
            val context = binding.clHeadlinerContainer.context
            val intent = Intent(context, DetailsActivity::class.java)

            val extras = Bundle()
            extras.putParcelable(ARTICLE_PARAM, article)

            intent.putExtras(extras)
            context.startActivity(intent)
        }
    }

}