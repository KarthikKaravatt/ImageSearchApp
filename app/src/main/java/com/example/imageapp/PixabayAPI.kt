package com.example.imageapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {
    @GET("/api/")
    fun searchImages(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("image_type") imageType: String
    ): Call<PixabayResponse>
}