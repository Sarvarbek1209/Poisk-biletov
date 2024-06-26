package com.example.poiskbiletov

import android.content.Context
import java.io.IOException
import java.io.InputStream

class ApiClientss(private val context: Context) {
    fun getOffersFromLocall1(): String? {
        return try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.search_tickets)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}