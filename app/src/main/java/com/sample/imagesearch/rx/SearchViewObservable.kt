package com.sample.imagesearch.rx

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * This class is observable class for search view, it provides search query from onQueryTextChange
 * and onQueryTextSubmit callback to observers
 */
object SearchViewObservable {
    /**
     * This methods returns {@link Observable} for given search view.
     * Clients can register for search query from search view to this observer
     * @param searchView SearchView to observe
     *
     * @return Observable returns {@link Observable} to listen for search query from passed search view
     */
    fun fromView(searchView: SearchView): Observable<String> {
        val subject = PublishSubject.create<String>()
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                subject.onNext(text)
                return true
            }
        })
        return subject
    }
}