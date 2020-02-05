package com.bleacher.reportclifford.list.api

import com.bleacher.reportclifford.Data.PhotoSearch
import io.reactivex.Single

class FlickrImageService(private val api: RetrofitApi) : ImageService {


    override fun listPhotos(): Single<PhotoSearch> {
        return api.listPhotos()
    }


    override fun searchPhotos(query: String): Single<PhotoSearch> {
        val formattedQuery = query.replace("/[,\\s]/".toRegex(), ",")

        return api.searchPhotos(formattedQuery)
    }

    companion object {
        val BASE_URL = "https://api.flickr.com"
    }
}