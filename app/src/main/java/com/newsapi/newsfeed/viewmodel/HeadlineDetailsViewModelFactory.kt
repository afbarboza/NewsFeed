package com.newsapi.newsfeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newsapi.newsfeed.model.Article

class HeadlineDetailsViewModelFactory(private val article: Article)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeadlineDetailsViewModel::class.java))
            return HeadlineDetailsViewModel(article) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}