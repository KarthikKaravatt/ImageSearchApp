package com.example.imageapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoViewModel : ViewModel() {
    private val _state = MutableStateFlow(PhotoState())
    private val _photos = MutableStateFlow(emptyList<Image>())
    val state = combine(_state, _photos) { state, photos ->
        state.copy(photos = photos)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PhotoState())

    fun onEvent(event: PhotoEvent) {
        when (event) {
            is PhotoEvent.Search -> {
                Log.d("CHECK_RESPONSE", "onEvent: ${_state.value.query}")
                _state.update {
                    it.copy(searchComplete = false)
                }
                PixabayService.searchImages(
                    query = _state.value.query,
                    object : Callback<PixabayResponse> {
                        override fun onResponse(
                            call: Call<PixabayResponse>,
                            response: Response<PixabayResponse>
                        ) {
                            if (response.isSuccessful) {
                                val imageResults = response.body()?.hits?.take(5)
                                val images = imageResults?.map {
                                    Image(url = it.largeImageURL)
                                }
                                _state.update {
                                    it.copy(photos = images ?: emptyList())
                                }
                                _photos.update {
                                   _state.value.photos
                                }
                                onEvent(PhotoEvent.SetQuery(""))
                                Log.d("CHECK_RESPONSE", "onResponse: ${response.body()}")
                            }
                            _state.update { it.copy(searchComplete = true) }
                            onEvent(PhotoEvent.SetQuery(""))
                        }

                        override fun onFailure(call: Call<PixabayResponse>, t: Throwable) {
                            Log.d("CHECK_RESPONSE", "onFailure: $t")
                            _state.update { it.copy(searchComplete = true) }
                        }
                    })

            }

            is PhotoEvent.SearchComplete -> {
                _state.update {
                    it.copy(searchComplete = true)
                }
            }

            is PhotoEvent.SwitchView -> TODO()
            is PhotoEvent.SetQuery -> {
                _state.update {
                    it.copy(query = event.query)
                }
            }
            is PhotoEvent.UpdatePhotos -> {
                _photos.update {
                    _state.value.photos
                }

            }
        }
    }
}