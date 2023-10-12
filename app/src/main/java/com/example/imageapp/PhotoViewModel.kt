package com.example.imageapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI

class PhotoViewModel : ViewModel() {
    private val _state = MutableStateFlow(PhotoState())
    private val _photos = MutableStateFlow(emptyList<Image>())
    val state = combine(_state, _photos) { state, photos ->
        state.copy(photos = photos)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PhotoState())

    fun onEvent(context: Context,event: PhotoEvent) {
        when (event) {
            is PhotoEvent.Search -> {
                Log.d("CHECK_RESPONSE", "onEvent: ${_state.value.query}")
                var images: List<Image>? = emptyList()
                _state.update {
                    it.copy(searchComplete = false)
                }
                _state.update {
                    it.copy(loading = true)
                }
                PixabayService.searchImages(
                    query = _state.value.query,
                    object : Callback<PixabayResponse> {
                        override fun onResponse(
                            call: Call<PixabayResponse>,
                            response: Response<PixabayResponse>
                        ) {
                            if (response.isSuccessful) {
                                val imageResults = response.body()?.hits?.take(15)
                                images = imageResults?.map {
                                    Image(url = it.largeImageURL, it.id)
                                }
                                Log.d("CHECK_RESPONSE", "onResponse: ${response.body()}")
                            }
                            _state.update {
                                it.copy(photos = images ?: emptyList())
                            }
                            _photos.update {
                                _state.value.photos
                            }
                            _state.update {
                                it.copy(loading = false)
                            }
                        }

                        override fun onFailure(call: Call<PixabayResponse>, t: Throwable) {
                            Log.d("CHECK_RESPONSE", "onFailure: $t")
                        }
                    })

                onEvent(context,PhotoEvent.SetQuery(""))


            }

            is PhotoEvent.SearchComplete -> {
                _state.update {
                    it.copy(searchComplete = true)
                }
            }

            is PhotoEvent.SwitchView -> {
                _state.update {
                    it.copy(switchView = !it.switchView)
                }
            }

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

            is PhotoEvent.Loading -> {
                _state.update {
                    it.copy(loading = !it.loading)
                }
            }

            is PhotoEvent.SelectImage -> {
                _state.update {
                    it.copy(selectedImage = event.image)
                }
            }

            is PhotoEvent.ShowUploadImageDialog -> {
                _state.update {
                    it.copy(showUploadImageDialog = !it.showUploadImageDialog)
                }
            }

            is PhotoEvent.UploadImage -> {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val imagesRef = storageRef.child("images")
                val imageRef = imagesRef.child(_state.value.selectedImage?.id.toString())
                val uploadTask = _state.value.selectedImage?.byteArray?.let { imageRef.putBytes(it) }

                uploadTask?.addOnSuccessListener {
                    // Create a Toast notification when the upload is successful
                    Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    Log.d("CHECK_UPLOAD", "onEvent: DONE ")
                }?.addOnFailureListener {
                    // Handle unsuccessful uploads
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}