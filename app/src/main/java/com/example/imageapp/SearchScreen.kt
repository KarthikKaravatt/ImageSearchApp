package com.example.imageapp

import android.annotation.SuppressLint
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SearchScreen() {
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
            Text(text = "Switch View")
        }
    }){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchBar()
            ImageList()
        }
    }
}

@Composable
private fun ImageList() {
    val images: List<Int> =
        listOf(R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground)
    LazyVerticalGrid(modifier = Modifier.padding(15.dp), columns = GridCells.Fixed(1), content = {
        items(images.size) { index ->
            Image(
                painter = painterResource(id = images[index]),
                contentDescription = null,
                modifier = Modifier.padding(5.dp)
            )
        }
    })
}

@Composable
private fun SearchBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchTextField()
        SearchButton()
    }
}

@Composable
private fun SearchButton() {
    Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Search")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.padding(5.dp),
        value = text,
        onValueChange = { text = it },
        label = { Text(text = "Search") })
}
