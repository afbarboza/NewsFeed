package com.newsapi.newsfeed.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.newsapi.newsfeed.databinding.ArticleViewHolderBinding
import com.newsapi.newsfeed.model.Article

class ArticleAdapter: PagingDataAdapter<Article, ArticleViewHolder>(DIFF_ITEM) {
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article: Article? = getItem(position)
        holder.bind(article!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        return ArticleViewHolder(
            ArticleViewHolderBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    companion object {
        private val DIFF_ITEM = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                val oldItemUrl = oldItem.url
                val newItemUrl = newItem.url
                return oldItemUrl.equals(newItemUrl)
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}