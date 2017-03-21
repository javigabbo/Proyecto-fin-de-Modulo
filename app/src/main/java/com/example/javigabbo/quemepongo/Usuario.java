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


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("email", email);
        result.put("categorias", categorias);
        return result;
    }


}
