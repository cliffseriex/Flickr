package com.bleacher.reportclifford.list.api

import com.bleacher.reportclifford.Data.PhotoSearch

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET(SEARCH_PATH)
    fun listPhotos(): Single<PhotoSearch>

    @GET(SEARCH_PATH)
    fun searchPhotos(@Query("tags") tags: String): Single<PhotoSearch>

    companion object {
        const val SEARCH_PATH = "/services/feeds/photos_public.gne?format=json&nojsoncallback=true"
    }

}