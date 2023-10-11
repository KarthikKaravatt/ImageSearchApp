package com.example.imageapp

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okio.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.io.readBytes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Image(private val url: String) {
    var byteArray: ByteArray? = null

    init {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            byteArray = try {
                val url = URL(url)
                url.readBytes()
            } catch (e: MalformedURLException) {
                Log.e("Image", "Malformed URL")
                null
            } catch (e: IOException) {
                Log.e("Image", "IO Exception")
                null
            }
            Log.d("Image", "Image loaded")
        }
    }
}