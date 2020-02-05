package com.bleacher.reportclifford.list.api

import com.bleacher.reportclifford.Data.PhotoSearch

import io.reactivex.Single

interface ImageService {

    fun listPhotos(): Single<PhotoSearch>

    fun searchPhotos(query: String): Single<PhotoSearch>

}