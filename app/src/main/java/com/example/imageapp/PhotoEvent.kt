package com.example.imageapp

sealed interface PhotoEvent {
    data class SetQuery(val query: String) : PhotoEvent
    object Search : PhotoEvent
    object SwitchView : PhotoEvent
    object SearchComplete : PhotoEvent
    object UpdatePhotos : PhotoEvent
}