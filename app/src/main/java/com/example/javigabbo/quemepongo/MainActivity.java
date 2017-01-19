package com.example.javigabbo.quemepongo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements GoogleSignInApi{


    LoginFragment loginFragment;
    RegistroFragment registroFragment;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Button btnIniciarSesion, btnRegistrarse, btnRegistro, btnIniciaSesion;
    EditText etNombre;

   // SingInButton googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // googleButton = (SingInButton) findViewById(R.id.googleButton);

        fm = getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragmentLogin);
        registroFragment = (RegistroFragment) fm.findFragmentById(R.id.fragmentRegistro);

        cambiarFragments(0);
    }


    public void cambiarFragments(int fragment){

        fragmentTransaction = fm.beginTransaction();

        if (fragment == 0){
            fragmentTransaction.show(loginFragment);
            fragmentTransaction.hide(registroFragment);


        }else if (fragment == 1){
            fragmentTransaction.show(loginFragment);


        } else if (fragment == 2){
            fragmentTransaction.show(registroFragment);

        }

        fragmentTransaction.commitNow();

    }


    @Override
    public Intent getSignInIntent(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public OptionalPendingResult<GoogleSignInResult> silentSignIn(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public PendingResult<Status> signOut(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public PendingResult<Status> revokeAccess(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public GoogleSignInResult getSignInResultFromIntent(Intent intent) {
        return null;
    }
}
