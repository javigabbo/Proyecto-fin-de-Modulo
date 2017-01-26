package com.example.javigabbo.quemepongo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Javigabbo on 26/1/17.
 */

@IgnoreExtraProperties
public class Objeto {

    public String color;
    public String nombre;

    public Objeto(String color) {}

    public Objeto(String color, String nombre) {
        this.color = color;
        this.nombre = nombre;
    }
}
