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

    private lateinit var loginViewModel: LoginViewModel
    var preferencias : SharedPreferences? = null
    var editPreferencias : SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)


        setContentView(R.layout.activity_login)

        preferencias = getSharedPreferences(
            "AndroidManifest.xml", Context.MODE_PRIVATE)

        editPreferencias = preferencias?.edit()


    }
        fun OnLogar(v:View){
            println("clicou")
            val username = findViewById<EditText>(R.id.username)
            val password = findViewById<EditText>(R.id.password)
            var nome = username.getText().toString()
            var senha = password.getText().toString()
            val logado = User(nome,senha)
            val task = LerPostagemTask()
            val Logou: Boolean? = task.execute(logado).get()
            println(Logou)
            println("-----------------------------------------------------------------")

            if(username.length()>10 && username!= null && password != null && password.length()>10)
            {
               if(checkbox.isChecked){
                   editPreferencias?.putBoolean("autenticado", true)
                   editPreferencias?.commit()

               }
            } else{
                Toast.makeText(this, "E-mail ou senha estão errados.", Toast.LENGTH_SHORT).show()

            }


           if(Logou!!) {
               var mapa = Intent(this, MapsActivity::class.java)
               startActivity(mapa)
           }
            else{
               Toast.makeText(this, "Erro na Autenticação", Toast.LENGTH_SHORT).show()
            }



        }
    inner  class LerPostagemTask : AsyncTask<User, Void, Boolean>() {
        override fun doInBackground(vararg params: User): Boolean? {
            val request = Feign.builder()
                .decoder(GsonDecoder())
                .target(
                    RequisicoesPostagem::class.java,
                    "http://192.168.0.17/renegates/api/"
                )

            try {
                return request.getPostagem(params[0].Nome!!,params[0].Senha!!)
            } catch (e:Exception) {
                println(e)
                return null
            }
        }
    }
    private fun handleJson(jsonString: String?) : Usuario{



        var jsonObject = JSONObject(jsonString)






        var usuario = Usuario(

            jsonObject.getString("nome"),

            jsonObject.getString("CPF"),

            jsonObject.getString("DataNascimento"),

            jsonObject.getString("sexo"),

            jsonObject.getString("estado_civil"),

            jsonObject.getString("CEP"),

            jsonObject.getString("Senha"),

            jsonObject.getInt("ID")

        )



        UsuarioLogado(usuario.ID,usuario.nome)
        println(usuario)
        return usuario

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
