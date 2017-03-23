package com.example.javigabbo.quemepongo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MyAdapter.RecyclerViewMyAdapterClickListener, ValueEventListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Usuario usuario;
    int indiceCategoria = 0;
    ProgressDialog progressDialog;
    boolean blpressed=false;
    public long time1=-1;
    ValueEventListener postListener;
    DrawerLayout drawerLayout;
    boolean hashMapVacio = true;
    ArrayList<Objeto> objetosCategoria;
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton;
    EditText nombrePrenda, tallaPrenda;
    int nombreUltimoObjeto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conjuntos");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        DataHolder.instance.firebaseUser = DataHolder.instance.firebaseAuth.getCurrentUser();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        floatingActionButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MenuActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.nueva_prenda_dialog, null);
                nombrePrenda = (EditText) mView.findViewById(R.id.prendaNombre);
                tallaPrenda = (EditText) mView.findViewById(R.id.prendaTalla);

                mBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (!nombrePrenda.getText().toString().equals("") && !tallaPrenda.getText().toString().equals("")){
                            uploadData(nombrePrenda.getText().toString(), tallaPrenda.getText().toString());
                        }

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });



                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.show();
                dialog.show();

            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressDialog.setTitle("Cargando datos...");
        progressDialog.show();



        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                objetosCategoria = new ArrayList<Objeto>();


                if (dataSnapshot.child("categorias").child(Integer.toString(indiceCategoria)).getValue(Categoria.class).objetos != null){

                    nombreUltimoObjeto = dataSnapshot.child("categorias").child(Integer.toString(indiceCategoria)).getValue(Categoria.class).objetos.size();
                    System.out.println("EL NUEVO OBJETO SE TIENE QUE LLAMAR: " + nombreUltimoObjeto);

                    for (int i = 0; i < dataSnapshot.child("categorias").child(Integer.toString(indiceCategoria)).getValue(Categoria.class).objetos.size(); i++){

                        String nombre = dataSnapshot.child("categorias").child(Integer.toString(indiceCategoria)).getValue(Categoria.class).objetos.get(i).getNombre();
                        System.out.println("NOMBREEEEE " + nombre);
                        String talla = dataSnapshot.child("categorias").child(Integer.toString(indiceCategoria)).getValue(Categoria.class).objetos.get(i).getTalla();
                        System.out.println("TALLAAAAAA " + talla);

                        //System.out.println("NOMBRE OBJETO " + dataSnapshot.child("categorias").child(Integer.toString(indiceCategoria)).getValue(Categoria.class).objetos.get(i).getNombre().toString());
                        //System.out.println("TALLA OBJETO " + dataSnapshot.child("categorias").child(Integer.toString(indiceCategoria)).getValue(Categoria.class).objetos.get(i).getTalla().toString());
                        objetosCategoria.add(new Objeto(nombre, talla));
                    }

                }else{
                    Toast.makeText(MenuActivity.this, "¡No tienes ropa en esta categoría!", Toast.LENGTH_SHORT).show();
                    nombreUltimoObjeto = 0;
                }



                    if (dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos != null){
                        System.out.println("Tiene cosas");
                        mAdapter = new MyAdapter(dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos,MenuActivity.this);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        DataHolder.instance.getuUserData(postListener);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_conjuntos) {
            indiceCategoria = 0;
            getSupportActionBar().setTitle("Conjuntos");
            DataHolder.instance.getuUserData(postListener);
            mRecyclerView.setAdapter(null);
            //mAdapter = new MyAdapter(usuario.categorias.get(indiceCategoria).objetos,this);
            //mRecyclerView.setAdapter(mAdapter);

        } else if (id == R.id.nav_camisetas) {
            indiceCategoria = 1;
            getSupportActionBar().setTitle("Camisetas");
            DataHolder.instance.getuUserData(postListener);

        } else if (id == R.id.nav_pantalones) {
            indiceCategoria = 2;
            getSupportActionBar().setTitle("Pantalones");
            DataHolder.instance.getuUserData(postListener);
            mRecyclerView.setAdapter(null);

        } else if (id == R.id.nav_chaquetas) {
            indiceCategoria = 4;
            getSupportActionBar().setTitle("Chaquetas");
            DataHolder.instance.getuUserData(postListener);
            mRecyclerView.setAdapter(null);

        } else if (id == R.id.nav_calzado) {
            indiceCategoria = 3;
            getSupportActionBar().setTitle("Calzado");
            DataHolder.instance.getuUserData(postListener);
            mRecyclerView.setAdapter(null);

        } else if (id == R.id.nav_logout){
            DataHolder.instance.firebaseAuth.signOut();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_perfil){
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void recyclerViewMyAdapterListClicked(View v, int position) {

        //Toast.makeText(this, "Pulsado: " + objetosCategoria.get(position).getNombre().toString(), Toast.LENGTH_SHORT).show();


      //  DataHolder.instance.setNombreItem(hashMapCategorias.get(indiceCategoria).get(position).);


        DataHolder.instance.setNombreItem(objetosCategoria.get(position).getNombre().toString());
        DataHolder.instance.setTallaItem(objetosCategoria.get(position).getTalla().toString());
        DataHolder.instance.setItemPosition(position);
        DataHolder.instance.setCategoria(indiceCategoria);

        startActivity(new Intent(MenuActivity.this, ElementoSeleccionadoActivity.class));

    }

    public void uploadData(String nombre, String talla){

        Toast.makeText(MenuActivity.this, "Se sube a la base de datos", Toast.LENGTH_SHORT).show();

        String refUrl = "https://quemepongo-6789b.firebaseio.com/usuarios/" + DataHolder.instance.firebaseUser.getUid()
                + "/categorias/" + indiceCategoria + "/objetos/" + nombreUltimoObjeto;


        DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                child(Integer.toString(indiceCategoria)).child("objetos").child(Integer.toString(nombreUltimoObjeto)).child("nombre").setValue(nombre);

        DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                child(Integer.toString(indiceCategoria)).child("objetos").child(Integer.toString(nombreUltimoObjeto)).child("talla").setValue(talla);


    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
