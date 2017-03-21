package com.example.javigabbo.quemepongo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MyAdapter.RecyclerViewMyAdapterClickListener {

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
    boolean hashMapLleno = false;
    HashMap<Integer, ArrayList<Objeto>> hashMapCategorias;
    ArrayList<Objeto> objetosCategoria;


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
        hashMapCategorias = new HashMap<>();
        DataHolder.instance.firebaseUser = DataHolder.instance.firebaseAuth.getCurrentUser();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */

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

                if (!hashMapLleno){

                    for (int i = 0; i< dataSnapshot.getValue(Usuario.class).categorias.size(); i++){
                        objetosCategoria = new ArrayList<Objeto>();
                        for(int j = 0; j < dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos.size(); j++){
                            objetosCategoria.add(dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos.get(j));
                        }
                        hashMapCategorias.put(indiceCategoria, objetosCategoria);
                    }

                    hashMapLleno = true;
                }


               // for (int i = 0; i< dataSnapshot.getValue(Usuario.class).categorias.size(); i++){
                   // System.out.println("Categoria: " + dataSnapshot.getValue(Usuario.class).categorias.get(i).nombre);

                    if (dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos == null){
                        Toast.makeText(MenuActivity.this, indiceCategoria+"", Toast.LENGTH_SHORT).show();
                        System.out.println("Vacio");

                    }else {
                        System.out.println("Tiene cosas");
                        Toast.makeText(MenuActivity.this, indiceCategoria+"", Toast.LENGTH_SHORT).show();
                        mAdapter = new MyAdapter(dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos,MenuActivity.this);
                        mRecyclerView.setAdapter(mAdapter);

                    //}

                }


                progressDialog.dismiss();

                System.out.println();


/*
                if (dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos.isEmpty()){
                    Toast.makeText(MenuActivity.this, "¡Aun no tienes nada!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                }else{
                    mAdapter = new MyAdapter(dataSnapshot.getValue(Usuario.class).categorias.get(indiceCategoria).objetos,MenuActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    progressDialog.dismiss();

                }
*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");


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
        ArrayList<Objeto> arr;
        arr = hashMapCategorias.get(indiceCategoria);

            Toast.makeText(this, "Nombre del item -> " + arr.get(position).getNombre().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Talla del item -> " + arr.get(position).getTalla().toString(), Toast.LENGTH_SHORT).show();


      //  DataHolder.instance.setNombreItem(hashMapCategorias.get(indiceCategoria).get(position).);


        //DataHolder.instance.setNombreItem(usuario.categorias.get(indiceCategoria).objetos.get(position).nombre);
        //DataHolder.instance.setTallaItem(usuario.categorias.get(indiceCategoria).objetos.get(position).talla);
        //DataHolder.instance.setItemPosition(position);

        //startActivity(new Intent(MenuActivity.this, ElementoSeleccionadoActivity.class));

    }
}
