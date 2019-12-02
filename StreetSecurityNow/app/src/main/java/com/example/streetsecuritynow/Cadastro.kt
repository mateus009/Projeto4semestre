package com.example.streetsecuritynow


import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.streetsecuritynow.HTTP.CADASTRO
import com.example.streetsecuritynow.HTTP.RequisicoesPostagem
import com.example.streetsecuritynow.R.id
import com.example.streetsecuritynow.ui.login.LoginActivity
import feign.Feign
import feign.gson.GsonEncoder
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_cadastro.view.*


class Cadastro : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
    }
    fun fodasseOUX(V:View)
    {

         var logar = Intent(this,LoginActivity::class.java)
         startActivity(logar)
    }


    fun confi(V:View){
        println("Chegou aqui <<<<<<<<<<<<<<<<<<<<<<<")
        var name = findViewById<EditText>(id.nomeCompleto).text.toString()
        var senha = findViewById<EditText>(id.password).text.toString()
        var campoCPF = findViewById<EditText>(id.CPF).text.toString()
        var campoDtNasc = findViewById<EditText>(id.dataNascimento).text.toString()
        var EstadoCivil = findViewById<Spinner>(id.estado).selectedItem.toString()
        var sexo = findViewById<Spinner>(id.sexo).selectedItem.toString()

            println("AQUI CARALHO > 1>>>>>>>>>"+name)
            println("AQUI CARALHO >2>>>>>>>>>"+senha)
            println("AQUI CARALHO >>>>>>>>>>"+campoCPF)
            println("AQUI CARALHO >>>>>>>>>>"+campoDtNasc)
            println("AQUI CARALHO >>>>>>>>>>"+sexo)
            println("AQUI CARALHO >>>>>>>>>>"+sexo)
            println("AQUI CARALHO >>>>>>>>>>"+EstadoCivil)



   val novaPostagem =
       CADASTRO(name,senha,campoCPF,campoDtNasc,EstadoCivil,sexo)
     val task = tsete()
     task.execute(novaPostagem)

   Toast.makeText(this,
     "Postagem criada com sucesso!", Toast.LENGTH_LONG).show()
    }


    inner class tsete: AsyncTask<CADASTRO, Void, Void?>() {

        override fun doInBackground(vararg params: CADASTRO): Void? {
            println(">>>>>>>>>" + getString(R.string.URL))
            val request = Feign.builder()
                //.decoder(GsonDecoder())
                .encoder(GsonEncoder())
                .target(
                    RequisicoesPostagem::class.java,
                    getString(R.string.URL)
                )
                println(params[0].name + " " +params[0].Senha+ " " +params[0].CPF+ " " +params[0].DataNascimento+ " " +params[0].estado_civil+ " " +params[0].sexo+ " ")
            try {
                request.criarPostagem(params[0].name!!,params[0].Senha!!,params[0].CPF!!,params[0].DataNascimento!!,params[0].estado_civil!!,params[0].sexo)
            } catch (e:Exception) {
                println(e)
                e.printStackTrace()
            }
            println("RETORNOU ALGO")
            return null
        }
    }
}