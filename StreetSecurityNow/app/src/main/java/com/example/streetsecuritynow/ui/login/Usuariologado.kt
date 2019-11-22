package com.example.streetsecuritynow.ui.login

class UsuarioLogado {

    companion object {

        // static count
        var idUsuario:Int=0

        var nomeUsuario:String = ""

    }



    constructor(id:Int, nome:String){

        idUsuario = id

        nomeUsuario = nome

    }

}