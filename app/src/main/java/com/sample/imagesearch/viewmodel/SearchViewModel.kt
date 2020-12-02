package com.sample.imagesearch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.domain.models.SearchError
import com.sample.domain.models.SearchResultModel
import com.sample.domain.usecases.SeachImagesUseCase
import com.sample.imagesearch.rx.SchedulersProvider
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException
import javax.inject.Inject

/**
 * This class is view model class for image search fragment.
 * Provides apis to get search result for search query entered by user
 */
class SearchViewModel @Inject constructor(
    private val searchImageUserCase: SeachImagesUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    /**
     * Search live data, client needs to register this live data to get search result back
     * from {@link getSearchResult} method
     */
    val searchLiveData = MutableLiveData<SearchResultModel>()
    private val compositeDisposable = CompositeDisposable()

    /**
     * This method makes network query using imgur API's to get search
     * result for given search query.
     * Client needs top observe searchLiveData to get search result
     */
    fun getSearchResult(queryString: String) {
        compositeDisposable.clear()
        searchImageUserCase.execute(queryString)
            .subscribeOn(schedulers.io())
            .subscribe({
                it?.let {
                    searchLiveData.postValue(it)
                }
            },{
                if (it is IOException) {
                    Log.e("TAG", "IOException")
                    searchLiveData.postValue(SearchResultModel.Failure(SearchError.NetworkError(-1)))
                } else {
                    Log.e("TAG", "ServerError")
                    searchLiveData.postValue(SearchResultModel.Failure(SearchError.ServerError(-1)))
                }
            }).let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}