package com.example.javigabbo.quemepongo;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MyAdapter.RecyclerViewMyAdapterClickListener, ValueEventListener {

    final Context context = this;
    private final int SELECT_PICTURE = 300;
    private final int PHOTO_CODE = 200;
    private String mPath;
    private StorageReference storageReference;
    Uri fotoUri;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int indiceCategoria = DataHolder.instance.I_CATEGORIA_CONJUNTOS;
    ProgressDialog progressDialog;
    ValueEventListener postListener;
    DrawerLayout drawerLayout;
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton;
    EditText nombrePrenda, tallaPrenda, descripcionPrenda;
    Usuario usr;
    ImageView imagePicked;
    TextView emailTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        storageReference = FirebaseStorage.getInstance().getReference();


        View dialogView = inflater.inflate(R.layout.nueva_prenda_dialog, null); //log.xml is your file.
        imagePicked = (ImageView) dialogView.findViewById(R.id.imagePicked);


        View vi = inflater.inflate(R.layout.nav_header_menu, null); //log.xml is your file.

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conjuntos");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        DataHolder.instance.firebaseUser = DataHolder.instance.firebaseAuth.getCurrentUser();

        System.err.println("POP_:________________________::::::::: " + DataHolder.instance.firebaseUser.getEmail());

        //emailTxt.setText(DataHolder.instance.firebaseUser.getEmail());

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        floatingActionButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.nueva_prenda_dialog);
                dialog.setCanceledOnTouchOutside(false);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                imagePicked = (ImageView)dialog.findViewById(R.id.imagePicked);
                nombrePrenda = (EditText) dialog.findViewById(R.id.prendaNombre);
                tallaPrenda = (EditText) dialog.findViewById(R.id.prendaTalla);
                descripcionPrenda = (EditText) dialog.findViewById(R.id.prendaDescripcion);

                Button dialogButtonOk = (Button) dialog.findViewById(R.id.btnOk);
                Button dialogButtonCancelar = (Button) dialog.findViewById(R.id.btnCancelar);


                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if (!nombrePrenda.getText().toString().equals("") && !tallaPrenda.getText().toString().equals("")) {



                            final ProgressDialog uploadDialog = new ProgressDialog(MenuActivity.this);
                            uploadDialog.setMessage("Guardando información. Por favor, espere");
                            uploadDialog.setCanceledOnTouchOutside(false);
                            uploadDialog.show();

                            if (fotoUri != null){
                                StorageReference filepath = storageReference.child("Photos").child(fotoUri.getLastPathSegment());
                                filepath.putFile(fotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        uploadDialog.dismiss();
                                        //Toast.makeText(MenuActivity.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                                        Uri downloadUri = taskSnapshot.getDownloadUrl();

                                        uploadData(nombrePrenda.getText().toString(), tallaPrenda.getText().toString(), downloadUri.toString(), descripcionPrenda.getText().toString());
                                        Toast.makeText(MenuActivity.this, descripcionPrenda.getText().toString(), Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        uploadDialog.dismiss();
                                        Toast.makeText(MenuActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else{
                                uploadData(nombrePrenda.getText().toString(), tallaPrenda.getText().toString(), "no image", descripcionPrenda.getText().toString());
                                uploadDialog.dismiss();
                            }

                            dialog.dismiss();
                        }else{
                            Toast.makeText(MenuActivity.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialogButtonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


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

        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();


        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println(dataSnapshot);
                usr = dataSnapshot.getValue(Usuario.class);

                if (usr.categorias.get(indiceCategoria).objetos == null) {
                    mRecyclerView.setAdapter(null);
                    Toast.makeText(MenuActivity.this, "No tienes elementos en esta categoría", Toast.LENGTH_SHORT).show();

                } else {
                    mAdapter = new MyAdapter(MenuActivity.this, usr.categorias.get(indiceCategoria).objetos, MenuActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }

                System.out.println("DATASNAPSHOT -> " + usr);

                DataHolder.instance.nombreUsuario = usr.nombre;
                DataHolder.instance.emailUsuario = usr.email;

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
            indiceCategoria = DataHolder.instance.I_CATEGORIA_CONJUNTOS;
            getSupportActionBar().setTitle("Conjuntos");
            DataHolder.instance.getuUserData(postListener);

        } else if (id == R.id.nav_camisetas) {
            indiceCategoria = DataHolder.instance.I_CATEGORIA_CAMISETAS;
            getSupportActionBar().setTitle("Camisetas");
            DataHolder.instance.getuUserData(postListener);

        } else if (id == R.id.nav_pantalones) {
            indiceCategoria = DataHolder.instance.I_CATEGORIA_PANTALONES;
            getSupportActionBar().setTitle("Pantalones");
            DataHolder.instance.getuUserData(postListener);

        } else if (id == R.id.nav_chaquetas) {
            indiceCategoria = DataHolder.instance.I_CATEGORIA_CHAQUETAS;
            getSupportActionBar().setTitle("Chaquetas");
            DataHolder.instance.getuUserData(postListener);

        } else if (id == R.id.nav_calzado) {
            indiceCategoria = DataHolder.instance.I_CATEGORIA_CALZADO;
            getSupportActionBar().setTitle("Calzado");
            DataHolder.instance.getuUserData(postListener);

        } else if (id == R.id.nav_logout) {
            DataHolder.instance.firebaseAuth.signOut();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void recyclerViewMyAdapterListClicked(View v, int position) {

        DataHolder.instance.setNombreItem(usr.categorias.get(indiceCategoria).objetos.get(position).getNombre());
        DataHolder.instance.setTallaItem(usr.categorias.get(indiceCategoria).objetos.get(position).getTalla());
        DataHolder.instance.setImagenItem(usr.categorias.get(indiceCategoria).objetos.get(position).getFoto());
        DataHolder.instance.setDescripcionItem(usr.categorias.get(indiceCategoria).objetos.get(position).getDescripcion());
        DataHolder.instance.setItemPosition(position);
        DataHolder.instance.setCategoria(indiceCategoria);

        startActivity(new Intent(MenuActivity.this, ElementoSeleccionadoActivity.class));
    }


    public void uploadData(String nombre, String talla, String foto, String descripcion) {

        if (usr.categorias.get(indiceCategoria).objetos == null) {
            //Toast.makeText(this, "No existe el array de objetos", Toast.LENGTH_SHORT).show();

            DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                    child(Integer.toString(indiceCategoria)).child("objetos").child("0").child("nombre").setValue(nombre);
            DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                    child(Integer.toString(indiceCategoria)).child("objetos").child("0").child("talla").setValue(talla);
            DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                    child(Integer.toString(indiceCategoria)).child("objetos").child("0").child("foto").setValue(foto);
            DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                    child(Integer.toString(indiceCategoria)).child("objetos").child("0").child("descripcion").setValue(descripcion);


        } else {

            usr.categorias.get(indiceCategoria).objetos.add(new Objeto(nombre, talla, foto, descripcion));
            Map<String, Object> postValues = usr.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/usuarios/" + DataHolder.instance.firebaseUser.getUid(), postValues);

            DataHolder.instance.mDatabase.updateChildren(childUpdates);
        }

    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                // la foto que se hace sale en el imagenview
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    imagePicked.setImageBitmap(bitmap);
                    break;

                // imagen de la galleria
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    imagePicked.setImageURI(path);
                    fotoUri = path;
                    //imagePicked.setImageDrawable(getResources().getDrawable(R.drawable.armario));

                    break;

            }
        }
    }

    public void onImageButtonClicked(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
    }

}
