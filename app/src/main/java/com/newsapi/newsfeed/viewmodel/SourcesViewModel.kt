package com.newsapi.newsfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsapi.newsfeed.BuildConfig
import com.newsapi.newsfeed.model.Source
import com.newsapi.newsfeed.model.SourcesList
import com.newsapi.newsfeed.repository.TopHeadlinesPageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SourcesViewModel(
    private val repository: TopHeadlinesPageRepository
): ViewModel() {
    private val _newsProviderName: MutableLiveData<String> = MutableLiveData()
    val newsProviderName: LiveData<String> = _newsProviderName

    private val _errorStatus: MutableLiveData<Boolean> = MutableLiveData()
    val errorStatus: LiveData<Boolean> = _errorStatus

    fun getHeadlinesPagingSource() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getAllSources()

            if (isErrorResponse(response)) {
                setErrorState()
            } else {
                val allSources = getAllSourcesFromResponse(response)
                setNewsProviderName(allSources)
            }
        }
    }

    private fun isErrorResponse(response: Response<SourcesList>): Boolean {
        return response.body() == null
                || !response.isSuccessful
                || response.body()?.sources.isNullOrEmpty()
    }

    private suspend fun setErrorState() {
        withContext(Dispatchers.Main) {
            _errorStatus.value = true
        }
    }

    private fun getAllSourcesFromResponse(response: Response<SourcesList>) :  List<Source> {
        return response.body()!!.sources
    }

    private suspend fun setNewsProviderName(allSources: List<Source>) {
        withContext(Dispatchers.Main) {
            _errorStatus.value = false

            val currentSource = allSources.first {
                it.id == BuildConfig.source_id
            }

            _newsProviderName.value = currentSource.name
        }
    }

}