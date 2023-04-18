package com.newsapi.newsfeed

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.newsapi.newsfeed.repository.TopHeadlinesPageRepository
import com.newsapi.newsfeed.viewmodel.TopHeadlinesViewModelFactory

object Injection {
    fun provideTopHeadlinesPageRepository(): TopHeadlinesPageRepository =  TopHeadlinesPageRepository()

    fun provideTopHeadlinesPageViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return TopHeadlinesViewModelFactory(owner, provideTopHeadlinesPageRepository())
    }
}