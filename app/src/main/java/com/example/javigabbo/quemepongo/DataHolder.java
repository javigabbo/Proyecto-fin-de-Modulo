package com.example.javigabbo.quemepongo;

import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Javigabbo on 6/2/17.
 */

public class DataHolder {

    private String TAG = "DataHolder";
    public FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    public static DataHolder instance = new DataHolder();
    public DatabaseReference mDatabase;

    public String nombreItem;
    public String tallaItem;
    public ImageView imagenItem;
    public int itemPosition;
    public int categoria;

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public DataHolder() {
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getuUserData(ValueEventListener postListener) {
        mDatabase.child("usuarios").child(firebaseUser.getUid()).addValueEventListener(postListener);
    }


    public void setImagenItem(ImageView imagenItem) {
        this.imagenItem = imagenItem;
    }

    public void setTallaItem(String tallaItem) {
        this.tallaItem = tallaItem;
    }

    public ImageView getImagenItem() {

        return imagenItem;
    }

    public String getTallaItem() {
        return tallaItem;
    }

    public void setNombreItem(String nItem) {
        nombreItem = nItem;
    }

    public String getNombreItem() {
        return nombreItem;
    }


}