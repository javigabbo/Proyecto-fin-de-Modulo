package com.example.javigabbo.quemepongo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Javigabbo on 26/1/17.
 */

@IgnoreExtraProperties
public class Categoria {

    public String nombre;
    public ArrayList<Objeto> objeto;

    public Categoria(){

    }

    public Categoria(String nombre, ArrayList<Objeto> objeto) {
        this.nombre = nombre;
        this.objeto = objeto;
    }
}
