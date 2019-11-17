package com.example.streetsecuritynow.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.streetsecuritynow.data.model.LoggedInUser
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val client = OkHttpClient()
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            fetchJson();
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "teste")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
    fun fetchJson(){

        val url = "https://localhost:44335/api/Default"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("${e?.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()
                println(body)

            }
        })

    }
}

