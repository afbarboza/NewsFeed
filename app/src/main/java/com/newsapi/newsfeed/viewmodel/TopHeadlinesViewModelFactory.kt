package com.newsapi.newsfeed.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.newsapi.newsfeed.repository.TopHeadlinesPageRepository

class TopHeadlinesViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: TopHeadlinesPageRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(TopHeadlinesPageViewModel::class.java)) {
            return TopHeadlinesPageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}