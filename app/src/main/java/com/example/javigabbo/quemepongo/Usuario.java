package com.example.javigabbo.quemepongo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Javigabbo on 26/1/17.
 */

@IgnoreExtraProperties
public class Usuario {

    public String nombre;
    public String email;
    public ArrayList <Categoria> categorias;

    public Usuario(){

    }

    public Usuario(String nombre, ArrayList<Categoria> categorias, String email) {
        this.nombre = nombre;
        this.categorias = categorias;
        this.email = email;
    }


}
