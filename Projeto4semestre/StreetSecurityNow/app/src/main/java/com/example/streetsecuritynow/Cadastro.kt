package com.example.streetsecuritynow


import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.streetsecuritynow.HTTP.CADASTRO
import com.example.streetsecuritynow.HTTP.RequisicoesPostagem
import com.example.streetsecuritynow.R.id
import feign.Feign
import feign.gson.GsonEncoder


class Cadastro : AppCompatActivity(){

@Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
    val sexos = resources.getStringArray(R.array.sexos)
    val sexo = findViewById(R.id.Sexo)
    if (sexo != null) {
        val adapter = ArrayAdapter (this,
        android.R.layout.activity_list_item, sexos)
        sexo.adapter = adapter
        sexo.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) =
                Toast.makeText(this@Activity_cadastro,
                    getString(R.string.selected_item) + " " +
                            "" + sexos[position], Toast.LENGTH_SHORT).show()

        }
    fun confirmar(V:View){

        println("Chegou aqui <<<<<<<<<<<<<<<<<<<<<<<")
        var name = findViewById<EditText>(id.NomeCompleto).text.toString()
        var senha = findViewById<EditText>(id.senha).text.toString()
        var campoCPF = findViewById<EditText>(id.CPF).text.toString()
        var campoDtNasc = findViewById<EditText>(id.campoDtNasc).text.toString()
        var EstadoCivil = "Indefinido"
        val validacao = ValidarGet()
        val existe:Boolean? = validacao.execute(name).get()

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
                    "http://192.168.0.17/renegates/"
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
    inner class ValidarGet: AsyncTask<String, Void, Boolean?>() {

        override fun doInBackground(vararg params: String): Boolean? {
            val request = Feign.builder()
                //.decoder(GsonDecoder())
                .encoder(GsonEncoder())
                .target(
                    RequisicoesPostagem::class.java,
                    "http://192.168.0.17/renegates/"
                )

            try {
                request.getExistsCadastro(params[0])
            } catch (e:Exception) {
                println(e)
                e.printStackTrace()
            }

            return null
        }

    }
}