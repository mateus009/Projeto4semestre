package com.example.streetsecuritynow.data

import android.os.AsyncTask
import android.widget.ListAdapter
import com.example.streetsecuritynow.HTTP.CADASTRO
import com.example.streetsecuritynow.data.model.LoggedInUser
import com.example.streetsecuritynow.data.model.User
import com.example.streetsecuritynow.data.model.Usuario
import okhttp3.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val client = OkHttpClient()
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val Login = User("TesteMao","123456")
            // TODO: handle loggedInUser authentication
            fetchJson()
            val url = "http://192.168.0.17/renegates/api/Usuarios?nome=TesteMao&senha=123456"
            GetJson().execute(url)
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "teste")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            println(e)
            return Result.Error(IOException("Error logging in", e))
        }
    }

    inner class GetJson : AsyncTask<String,String,String>(){
        override fun doInBackground(vararg url: String?): String {
            var text:String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            }finally {
                connection.disconnect()
            }
            return text

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }

        private fun handleJson(jsonString: String?) {
            val jsonArray = JSONArray(jsonString)
            val list = ArrayList<Usuario>()
            var x =0;
            while (x < jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(x)
                list.add(Usuario(
                    jsonObject.getString("nome"),
                    jsonObject.getString("CPF"),
                    jsonObject.getString("DataNascimento"),
                    jsonObject.getString("sexo"),
                    jsonObject.getString("estado_civil"),
                    jsonObject.getString("CEP"),
                    jsonObject.getString("Senha"),
                    jsonObject.getInt("ID")
                ))
                x++
            }
            println(list)
        }
    }
    fun logout() {
        // TODO: revoke authentication
    }
    fun fetchJson(){

    }

}



