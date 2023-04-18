package com.newsapi.newsfeed.view

import android.view.RoundedCorner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.newsapi.newsfeed.databinding.ArticleViewHolderBinding
import com.newsapi.newsfeed.model.Article

class ArticleViewHolder(
    private val binding: ArticleViewHolderBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article) {
        binding.tvHeadlineTitle.text = article.title
        binding.tvHeadlineDescription.text = article.title
        binding.tvHeadlinePublishedAt.text = article.publishedAt

        Glide.with(binding.root)
            .load(article.urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivHeadlineImage)
    }
}