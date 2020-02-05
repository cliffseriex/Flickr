package com.bleacher.reportclifford.list

import com.bleacher.reportclifford.Data.PhotoSearchResultItem

internal interface ImageListView {

    fun showImages(images: List<PhotoSearchResultItem>)

    fun showLoading(show: Boolean)

    fun showConnectionError()

    fun showGenericError()
}