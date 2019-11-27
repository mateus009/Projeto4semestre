package com.example.streetsecuritynow.ui.login

data class Login (

    var nome:String?,

    var senha:String?

){
    constructor():this("","")



    fun toJson() : String{

        return(

                """{"nome" : "${this.nome}",

                "senha" : "${this.senha}"}""")

    }

}