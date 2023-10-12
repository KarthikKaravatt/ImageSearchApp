package com.example.imageapp

data class PhotoState(
    val photos: List<Image> = emptyList(),
    val query: String = "",
    val switchView: Boolean = false,
    val searchComplete: Boolean = true,
    val loading: Boolean = true,
    val selectedImage: Image? = null,
    val showUploadImageDialog: Boolean = false,
    val progress: Float = 0f,
)