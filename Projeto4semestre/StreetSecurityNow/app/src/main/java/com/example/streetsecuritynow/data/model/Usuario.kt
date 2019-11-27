package com.example.streetsecuritynow.data.model

import java.util.*

data class Usuario(
    val nome: String,
    val CPF: String ,
    val DataNascimento : String,
    val sexo: String,
    val estado_civil: String,
    val CEP: String ,
    val Senha: String,
    val ID: Int
)