package br.edu.utpr.controlefluxocaixa.entity

data class Cadastro(
    var _id: Int,
    var tipo: String,
    var detalhe: String,
    var valor: Float,
    var data : String
)
