package com.example.javigabbo.quemepongo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
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
    Button btnIniciarSesion, btnRegistrarse;
    EditText etEmailLogin, etPasswordLogin, etNombreRegistro, etEmailRegistro, etPasswordRegistro;
    TextView tvRegistrarse, tvIniciarSesion;
    Controlador controlador;
    ProgressDialog progressDialog;

   // SingInButton googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // googleButton = (SingInButton) findViewById(R.id.googleButton);
        controlador = new Controlador(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        fm = getSupportFragmentManager();

        loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragmentLogin);
        registroFragment = (RegistroFragment) fm.findFragmentById(R.id.fragmentRegistro);

        btnIniciarSesion = (Button)loginFragment.getView().findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = (Button)registroFragment.getView().findViewById(R.id.btnRegistrarse);

        tvIniciarSesion = (TextView) registroFragment.getView().findViewById(R.id.tvIniciarSesion);
        tvRegistrarse = (TextView) loginFragment.getView().findViewById(R.id.tvRegistrarse);

        etEmailLogin = (EditText)loginFragment.getView().findViewById(R.id.etEmailLogin);
        etPasswordLogin = (EditText)loginFragment.getView().findViewById(R.id.etPasswordLogin);
        etNombreRegistro = (EditText)registroFragment.getView().findViewById(R.id.etNombRegistro);
        etEmailRegistro = (EditText)registroFragment.getView().findViewById(R.id.etEmailRegistro);
        etPasswordRegistro = (EditText)registroFragment.getView().findViewById(R.id.etPasswordRegistro);

        btnIniciarSesion.setOnClickListener(controlador);
        btnRegistrarse.setOnClickListener(controlador);
        tvIniciarSesion.setOnClickListener(controlador);
        tvRegistrarse.setOnClickListener(controlador);

        DataHolder.instance.firebaseAuth = FirebaseAuth.getInstance();
        if (DataHolder.instance.firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        }

        cambiarFragments(0);

        /*
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", "javigabbo");
        editor.putString("password", "12345");
        editor.commit();
        String user = settings.getString("username", null);
        String pass = settings.getString("password", null);
        */

    }


    public void cambiarFragments(int fragment) {

        fragmentTransaction = fm.beginTransaction();

        if (fragment == 0) {
            fragmentTransaction.show(loginFragment);
            fragmentTransaction.hide(registroFragment);
            fragmentTransaction.commitNow();
        }

        if (fragment == 1) {
            fragmentTransaction.hide(loginFragment);
            fragmentTransaction.show(registroFragment);
            fragmentTransaction.commitNow();
        }
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
