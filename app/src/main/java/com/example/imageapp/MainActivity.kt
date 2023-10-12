package com.example.imageapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imageapp.ui.theme.ImageAppTheme
import com.google.firebase.storage.FirebaseStorage

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<PhotoViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PhotoViewModel() as T
                }
            }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            ImageAppTheme {
                val state by viewModel.state.collectAsState()
                SearchScreen(state = state, onEvent = {
                    viewModel.onEvent(context,it)
                })

            }
        }
    }
}
