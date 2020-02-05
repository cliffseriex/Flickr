package com.bleacher.reportclifford.list

import com.bleacher.reportclifford.list.api.ImageService
import com.bleacher.reportclifford.Data.PhotoSearch
import io.reactivex.Scheduler
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.net.UnknownHostException

internal class ImageListPresenter(
        private var view: ImageListView?,
        private val imageService: ImageService,
        private val viewScheduler: Scheduler,
        private val backgroundScheduler: Scheduler) {

    private var currentSearchResults: PhotoSearch? = null

    fun requestImages() {
        view!!.showLoading(true)

        imageService.listPhotos()
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(hideLoading())
                .subscribe(onSuccess(), onError())
    }

    fun requestImages(query: String) {
        view!!.showLoading(true)

        imageService.searchPhotos(query)
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(hideLoading())
                .subscribe(onSuccess(), onError())
    }

    private fun onSuccess(): Consumer<PhotoSearch> {
        return Consumer { photoSearchResult ->
            currentSearchResults = photoSearchResult
            view!!.showImages(photoSearchResult.items)
        }
    }

    private fun onError(): Consumer<Throwable> {
        return Consumer { throwable ->
            if (throwable is UnknownHostException) {
                view!!.showConnectionError()
            } else {
                view!!.showGenericError()
            }
        }
    }

    private fun hideLoading(): Action {
        return Action { view!!.showLoading(false) }
    }

    fun onDestroy() {
        this.view = null
    }



    companion object {
        val DATE_TAKEN_DESC_SORT_TYPE = 0
        val DATE_PUBLISHED_DESC_SORT_TYPE = 1
    }
}