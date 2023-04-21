package com.newsapi.newsfeed.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsapi.newsfeed.model.Article

class HeadlineDetailsViewModel(
    article: Article
) : ViewModel() {
    @VisibleForTesting
    var articleData: MutableLiveData<Article?> = MutableLiveData()
    init {
        articleData.value = article
    }
}