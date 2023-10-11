package com.example.imageapp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PixabayService {
    val KEY = "39978021-cc3b49c6c3dbe7d120da08ec8"
    val IMAGE_TYPE = "photo"
    private val api: PixabayAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pixabay.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PixabayAPI::class.java)
    }

    fun searchImages(query: String, callback: Callback<PixabayResponse>) {
        query.replace(" ", "+")
        val call = api.searchImages(KEY, query, IMAGE_TYPE)
        call.enqueue(callback)
    }
}
