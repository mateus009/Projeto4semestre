package com.example.streetsecuritynow


import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.streetsecuritynow.HTTP.CADASTRO
import com.example.streetsecuritynow.HTTP.RequisicoesPostagem
import com.example.streetsecuritynow.R.id
import feign.Feign
import feign.gson.GsonEncoder


class Cadastro : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
    }
    fun confirmar(V:View){
        println("Chegou aqui <<<<<<<<<<<<<<<<<<<<<<<")
        var name = findViewById<EditText>(id.campoNomeCompleto).text.toString()
        var senha = findViewById<EditText>(id.senha).text.toString()
        var campoCPF = findViewById<EditText>(id.campoCPF).text.toString()
        var campoDtNasc = findViewById<EditText>(id.campoDtNasc).text.toString()
        var EstadoCivil = "Indefinido"


        //sendPostRequest(name,senha,cpf,nascimento)
        val novaPostagem =
            CADASTRO(name,senha,campoCPF,campoDtNasc,EstadoCivil)
        val task = CriarPostagemTask()
        task.execute(novaPostagem)

        Toast.makeText(this,
            "Postagem criada com sucesso!", Toast.LENGTH_SHORT).show()
    }

    inner class CriarPostagemTask: AsyncTask<CADASTRO, Void, Void>() {

        override fun doInBackground(vararg params: CADASTRO): Void? {
            val request = Feign.builder()
                //.decoder(GsonDecoder())
                .encoder(GsonEncoder())
                .target(
                    RequisicoesPostagem::class.java,
                    getString(R.string.URL)
                )

            try {
                request.criarPostagem(params[0].name!!,params[0].Senha!!,params[0].CPF!!,params[0].DataNascimento!!,params[0].estado_civil!!)
            } catch (e:Exception) {
                println(e)
                e.printStackTrace()
            }

            return null
        }
    }
}