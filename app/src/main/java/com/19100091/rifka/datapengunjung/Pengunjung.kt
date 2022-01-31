package com.example.py7.crudkotlin

class Pengunjung {

    var id: Int? = null
    var name: String? = null
    var jenis: String? = null
    var alamat: String? = null

    constructor(id: Int, name: String, jenis: String, alamat:String){
        this.id = id
        this.name = name
        this.jenis = jenis
        this.alamat = alamat
    }
}