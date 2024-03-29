package com.example.streetsecuritynow.HTTP

import com.example.streetsecuritynow.data.model.User
import com.example.streetsecuritynow.data.model.Usuario
import feign.Headers
import feign.Param
import feign.RequestLine

interface RequisicoesPostagem {
    @RequestLine("GET api/Usuarios?nome={nome}&senha={senha}")
    fun getPostagem(@Param("nome") nome:String, @Param("senha") senha:String): Boolean

    // exemplo de GET que traz uma lista
    @RequestLine("GET /Usuarios/{id}")
    fun getPostagens(@Param("id") id:Int): Usuario


    @RequestLine("GET api//Usuarios")
    fun getExists(user:User) : Boolean
    // exemplo de POST
    @RequestLine("POST /meio?nome={nome}&Senha={Senha}&CPF={CPF}&DataNascimento={DataNascimento}&estadoCivil={estado_civil}&sexo={sexo}")
    fun criarPostagem(@Param("nome") nome:String,
                      @Param("Senha") senha:String,
                      @Param("CPF") CPF:String,
                      @Param("DataNascimento") DataNascimento:String,
                      @Param("estado_civil") estado_civil:String,
                      @Param("sexo") sexo:String)

    // exemplo de DELETE
    @RequestLine("DELETE /posts/{id}")
    fun deletePostagem(@Param("id") id: Int)
}