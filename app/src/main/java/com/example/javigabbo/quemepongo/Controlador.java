package com.example.javigabbo.quemepongo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by Javigabbo on 19/1/17.
 */

public class Controlador implements View.OnClickListener{

    MainActivity vista;
    Intent intent = null;

    public Controlador(MainActivity view){
        this.vista = view;
        intent = new Intent(vista, MenuActivity.class);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == vista.tvRegistrarse.getId()){
            vista.cambiarFragments(1);
        }
        if (v.getId() == vista.tvIniciarSesion.getId()){
            vista.cambiarFragments(0);
        }

        if (v.getId() == vista.btnIniciarSesion.getId()){
            iniciarSesion(vista.etEmailLogin.getText().toString().trim(), vista.etPasswordLogin.getText().toString().trim());
        }

        if (v.getId() == vista.btnRegistrarse.getId()){
            registrarse(vista.etNombreRegistro.getText().toString().trim(), vista.etEmailRegistro.getText().toString(), vista.etPasswordRegistro.getText().toString().trim());
        }
    }



    public void iniciarSesion(String email, String password){

        if(TextUtils.isEmpty(email)){
            Toast.makeText(vista, "Por favor, introduce el email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(vista, "Por favor, introduce la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        vista.progressDialog.setMessage("Iniciando sesión...");
        vista.progressDialog.show();
        DataHolder.instance.firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(vista, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    vista.progressDialog.dismiss();
                    if (task.isSuccessful()){
                        //Se almacena el usuario actual
                        DataHolder.instance.firebaseUser = DataHolder.instance.firebaseAuth.getInstance().getCurrentUser();
                        //Usuario se ha logeado con exito
                        vista.progressDialog.dismiss();
                        Toast.makeText(vista, "Logeado con éxito", Toast.LENGTH_SHORT).show();
                        vista.startActivity(intent);
                        vista.finish();
                    }else {
                        //Problema durante el login
                        vista.progressDialog.dismiss();

                        Toast.makeText(vista, "Problema al logear, compruebe los datos o la conexión a Internet", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }




    public void registrarse(final String nombre, String email, String password){

        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(vista, "Por favor, introduce el nombre", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(vista, "Por favor, introduce el email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(vista, "Por favor, introduce la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        vista.progressDialog.setMessage("Registrando usuario...");
        vista.progressDialog.show();
        DataHolder.instance.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(vista, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    //Usuario se ha registrado con exito y ha iniciado sesion
                    vista.progressDialog.dismiss();
                    Toast.makeText(vista, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show();
                    //String key = DataHolder.instance.mDatabase.child("usuarios").push().getKey();
                    DataHolder.instance.firebaseUser = DataHolder.instance.firebaseAuth.getInstance().getCurrentUser();
                    String userID=DataHolder.instance.firebaseUser.getUid();
                    Usuario usuario = new Usuario(nombre, generarCategorias(), DataHolder.instance.firebaseUser.getEmail());

                    Map<String, Object> childUpdates = new HashMap<String, Object>();
                    childUpdates.put("/usuarios/" + userID, usuario.toMap());
                    //childUpdates.put("/usuarios/" + key + "/", categoria.toMap());
                    DataHolder.instance.mDatabase.updateChildren(childUpdates);

                    vista.startActivity(intent);
                    vista.finish();
                }else {
                    System.out.println("ERROR REGISTRO:::::::::::::::::: " + task.getException());
                    //Problema durante el registro
                    vista.progressDialog.dismiss();
                    Toast.makeText(vista, "No se ha podido registrar el usuario, por favor inténtalo más tarde", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public ArrayList<Categoria> generarCategorias(){
        ArrayList<Categoria> cats=new ArrayList<Categoria>();
        for (int i = 0; i <= 4; i++){
            if (i == 0){
                cats.add(new Categoria("conjuntos",new ArrayList<Objeto>()));
            }else if (i == 1){
                cats.add(new Categoria("camisetas",new ArrayList<Objeto>()));
            }else if (i == 2){
                cats.add(new Categoria("pantalones",new ArrayList<Objeto>()));
            }else if (i == 3){
                cats.add(new Categoria("calzados",new ArrayList<Objeto>()));
            }else if (i == 4){
                cats.add(new Categoria("chaquetas",new ArrayList<Objeto>()));
            }

        }


        return cats;
    }


}
