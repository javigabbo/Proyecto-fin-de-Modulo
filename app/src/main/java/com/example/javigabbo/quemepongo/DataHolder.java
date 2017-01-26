package com.example.javigabbo.quemepongo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Javigabbo on 26/1/17.
 */

@IgnoreExtraProperties
public class DataHolder{

    private DatabaseReference mDatabase;
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}



