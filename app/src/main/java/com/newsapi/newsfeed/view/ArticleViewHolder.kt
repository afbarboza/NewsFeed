package com.newsapi.newsfeed.view

import androidx.recyclerview.widget.RecyclerView
import com.newsapi.newsfeed.databinding.ArticleViewHolderBinding
import com.newsapi.newsfeed.model.Article

class ArticleViewHolder(
    private val binding: ArticleViewHolderBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article) {
        binding.tvTitle.text = article.title
    }
}