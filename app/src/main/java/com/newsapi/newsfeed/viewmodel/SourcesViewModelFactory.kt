package com.newsapi.newsfeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newsapi.newsfeed.repository.TopHeadlinesPageRepository

class SourcesViewModelFactory(private val repository: TopHeadlinesPageRepository)
    :   ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SourcesViewModel::class.java))
            return SourcesViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}