package com.example.javigabbo.quemepongo;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Javigabbo on 26/1/17.
 */

@IgnoreExtraProperties
public class Categoria {

    public String nombre;
    public ArrayList<Objeto> objetos;

    public Categoria(){}


    public Categoria(String nombre, ArrayList<Objeto> objetos) {
        this.nombre = nombre;
        this.objetos = objetos;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("objetos", objetos);
        return result;
    }
}
