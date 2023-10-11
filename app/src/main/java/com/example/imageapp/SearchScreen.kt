package com.example.imageapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SearchScreen(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
            Text(text = "Switch View")
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchBar(state = state, onEvent = onEvent)
            ImageList(state = state, onEvent = onEvent)
        }
    }
}

@Composable
private fun ImageList(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    val images = state.photos
    LazyVerticalGrid(
        modifier = Modifier.padding(15.dp),
        columns = GridCells.Fixed(1),
        content = {
            items(images.size) { index ->
                val imageBitMap = images[index].byteArray?.let {
                    BitmapFactory.decodeByteArray(
                        images[index].byteArray,
                        0,
                        it.size
                    )
                }
                if (imageBitMap != null) {
                    Image(
                        bitmap = imageBitMap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        })
}

@Composable
private fun SearchBar(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchTextField(state = state, onEvent = onEvent)
        SearchButton(state, onEvent = onEvent)
    }
}

@Composable
private fun SearchButton(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    Button(
        onClick = {
            if (state.searchComplete) {
                onEvent(PhotoEvent.Search)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Search")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    val text = state.query
    OutlinedTextField(
        modifier = Modifier.padding(5.dp),
        value = text,
        onValueChange = { onEvent(PhotoEvent.SetQuery(it)) },
        label = { Text(text = "Search") })
}
