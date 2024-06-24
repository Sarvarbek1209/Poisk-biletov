package com.example.poiskbiletov



import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


fun main() {
    val url = "https://run.mocky.io/v3/ad9a46ba-276c-4a81-88a6-c068e51cce3a"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).execute().use { response: Response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println("Response Code: ${response.code}")
        println("Response Body: ${response.body?.string()}")
    }
}
