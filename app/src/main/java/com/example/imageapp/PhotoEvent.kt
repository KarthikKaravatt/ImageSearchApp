package com.example.imageapp

sealed interface PhotoEvent {
    data class SetQuery(val query: String) : PhotoEvent
    object Search : PhotoEvent
    object SwitchView : PhotoEvent
    object SearchComplete : PhotoEvent
    object UpdatePhotos : PhotoEvent
    object UploadImage : PhotoEvent
    object Loading : PhotoEvent
    data class SelectImage(val image: Image) : PhotoEvent
    data class ShowUploadImageDialog(val boolean: Boolean) : PhotoEvent
}