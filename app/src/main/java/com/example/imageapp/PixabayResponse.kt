package com.example.imageapp

data class PixabayResponse(
    val totalHits: Int,
    val hits: List<ImageResult>,
    val total: Int
)

data class ImageResult(
    val id: Int,
    val largeImageURL: String,
    val webformatURL: String,
    val webformatHeight: Int,
    val webformatWidth: Int,
)
