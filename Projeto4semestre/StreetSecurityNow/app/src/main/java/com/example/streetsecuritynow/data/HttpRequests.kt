package com.example.streetsecuritynow.data

import android.R.string
import android.os.AsyncTask.execute
import okhttp3.Request
import java.io.IOException
import okhttp3.OkHttpClient




class HttpRequests {
   private var client = OkHttpClient()
    @Throws(IOException::class)
    fun run(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use({ response -> return response.body.toString()})
    }
}