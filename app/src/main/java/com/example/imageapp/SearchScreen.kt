package com.example.imageapp

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SearchScreen(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    if (state.showUploadImageDialog) {
        UploadImageDialog(state = state, onEvent = onEvent)
    }
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { onEvent(PhotoEvent.SwitchView) }) {
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
fun UploadImageDialog(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    val bitmap = state.selectedImage?.byteArray?.let {
        BitmapFactory.decodeByteArray(
            state.selectedImage.byteArray,
            0,
            it.size
        )
    }
    Dialog(onDismissRequest = { onEvent(PhotoEvent.ShowUploadImageDialog(false)) }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black),
        ) {

            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )
                }
                Button(onClick = { onEvent(PhotoEvent.UploadImage) }) {
                    Text(text = "Upload")
                }
            }

        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageList(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    val images = state.photos
    var cellCount = 1
    if (state.switchView) {
        cellCount = 2
    }
    if (state.searchComplete) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(15.dp),
                columns = StaggeredGridCells.Fixed(cellCount),
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
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        Log.d("CHECK_IMAGE", "ImageList: ${images[index]}")
                                        onEvent(PhotoEvent.SelectImage(images[index]))
                                        onEvent(PhotoEvent.ShowUploadImageDialog(true))
                                    }
                            )
                        }
                    }
                })

        }
    } else {
        if (state.loading) {
            Text(text = "Loading...")
        } else {
            Button(onClick = { onEvent(PhotoEvent.SearchComplete) }) {
                Text(text = "Refresh")

            }
        }
    }
}

@Composable
private fun SearchBar(state: PhotoState, onEvent: (PhotoEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchTextField(modifier = Modifier.weight(0.75f), state = state, onEvent = onEvent)
        SearchButton(onEvent = onEvent, modifier = Modifier.weight(0.25f))
    }
}

@Composable
private fun SearchButton(onEvent: (PhotoEvent) -> Unit, modifier: Modifier) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onEvent(PhotoEvent.Search)
        },
    ) {
        Text(text = "Search")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(state: PhotoState, onEvent: (PhotoEvent) -> Unit, modifier: Modifier) {
    val text = state.query
    OutlinedTextField(
        modifier = modifier.padding(5.dp),
        value = text,
        onValueChange = { onEvent(PhotoEvent.SetQuery(it)) },
        label = { Text(text = "Search") })
}
