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
            //iniciarsesion(usuario, password);
        }

        if (v.getId() == vista.btnRegistrarse.getId()){
            registrarse(vista.etEmailRegistro.getText().toString(), vista.etPasswordRegistro.getText().toString());
        }
    }



    public void iniciarSesion(String email, String password){}




    public void registrarse(String email, String password){

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
        vista.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(vista, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    //Usuario se ha registrado con exito y ha iniciado sesion
                    vista.progressDialog.hide();
                    Toast.makeText(vista, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show();
                    vista.startActivity(intent);
                    vista.finish();
                }else {
                    //Problema durante el registro
                    Toast.makeText(vista, "No se ha podido registrar el usuario, por favor inténtalo más tarde", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


}
