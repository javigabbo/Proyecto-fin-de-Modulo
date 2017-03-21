package com.example.javigabbo.quemepongo;

import android.media.Image;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Javigabbo on 26/1/17.
 */

@IgnoreExtraProperties
public class Objeto {

    public String talla;
    public String nombre;
    //public Image foto;


    public Objeto(){}

    public Objeto(String talla, String nombre) {
        this.talla = talla;
        this.nombre = nombre;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("talla", talla);
        return result;
    }

}
