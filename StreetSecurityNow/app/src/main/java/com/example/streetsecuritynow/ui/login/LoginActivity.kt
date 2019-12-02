package com.example.streetsecuritynow.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.streetsecuritynow.Cadastro
import com.example.streetsecuritynow.HTTP.RequisicoesPostagem
import com.example.streetsecuritynow.MapsActivity

import com.example.streetsecuritynow.R
import com.example.streetsecuritynow.data.model.User
import com.example.streetsecuritynow.data.model.Usuario
import feign.Feign
import feign.gson.GsonDecoder
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    var preferencias : SharedPreferences? = null
    var editPreferencias : SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)


        setContentView(R.layout.activity_login)

        preferencias = getSharedPreferences(
            "Alguma_Coisa", Context.MODE_PRIVATE)

        editPreferencias = preferencias?.edit()
        if (preferencias!!.getBoolean("autenticado", false)) {
            var mapa = Intent(this, MapsActivity::class.java)
        //startActivity(mapa)
        }

    }
        fun OnLogar(v:View){
            println("clicou")
            val username = findViewById<EditText>(R.id.username)
            val password = findViewById<EditText>(R.id.password)
            var nome = username.getText().toString()
            var senha = password.getText().toString()
            val logado = User(nome,senha)
            var Logou: Boolean = false
            try {
                val task = LerPostagemTask()
                Logou = task.execute(logado).get()

                if(checkbox.isChecked)
                {
                    editPreferencias?.putBoolean("autenticado", true)
                    editPreferencias?.commit()
                }
            }catch (e:Exception) {
                println(e)
                Toast.makeText(this,
                    "Ocorreu um erro",Toast.LENGTH_SHORT).show()
            }

           if(Logou){
               var mapa = Intent(this, MapsActivity::class.java)
                startActivity(mapa)

            }else
           {
               println("porraaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
               Toast.makeText(this,
                   "Login ou senha incorretos",Toast.LENGTH_LONG).show()
           }



        }
    fun cadastrar(V:View)
    {

    var cadastrar = Intent(this,Cadastro::class.java)
        startActivity(cadastrar)
    }

    inner  class LerPostagemTask : AsyncTask<User, Void, Boolean>() {
        override fun doInBackground(vararg params: User): Boolean? {
            try {
            val request = Feign.builder()
                .decoder(GsonDecoder())
                .target(
                    RequisicoesPostagem::class.java,
                    getString(R.string.URL)
                )
            println(" >>>>>>>>>>>>>>>>>>> request  "+request )


                return request.getPostagem(params[0].Nome!!,params[0].Senha!!)
            } catch (e:Exception) {
                println(e)
                return false
            }
        }
    }


}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
