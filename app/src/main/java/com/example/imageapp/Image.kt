package com.example.imageapp

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import okio.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.io.readBytes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class Image(val url: String, val id: Int) {
    var byteArray: ByteArray? = null

    init {
        val scope = CoroutineScope(Dispatchers.Default)
        // Use async to create a Deferred object
        scope.launch {
            byteArray = async {
                try {
                    val url = URL(url)
                    url.readBytes()
                } catch (e: MalformedURLException) {
                    Log.e("Image", "Malformed URL")
                    null
                } catch (e: IOException) {
                    Log.e("Image", "IO Exception")
                    null
                }
            }.await() // Now this is inside a coroutine
        }
    }
}
