package com.example.imageapp

import kotlinx.coroutines.sync.Mutex

data class PhotoState(
    val photos: List<Image> = emptyList(),
    val query: String = "",
    val switchView: Boolean = false,
    val searchComplete: Boolean = true,
    val loading: Boolean = false,
    val selectedImage: Image? = null,
    val showUploadImageDialog: Boolean = false,
    val progress: Float = 0f,
    val lock: Mutex = Mutex()
)