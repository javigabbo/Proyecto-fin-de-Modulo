package com.example.javigabbo.quemepongo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ElementoSeleccionadoActivity extends AppCompatActivity {

    EditText nombreItem, tallaItem, descripcionItem;
    ImageView imagenItem;
    Button btnCancelarEdicion;
    Button btnConfirmarEdicion;
    FrameLayout layout;
    com.getbase.floatingactionbutton.FloatingActionButton fab_editar;
    com.getbase.floatingactionbutton.FloatingActionButton fab_eliminar;
    com.getbase.floatingactionbutton.FloatingActionButton fab_compartir;
    com.getbase.floatingactionbutton.FloatingActionsMenu fab_menu;

    Objeto objeto;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_elemento_seleccionado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(DataHolder.instance.getNombreItem());


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        nombreItem = (EditText) findViewById(R.id.tvNombre);
        tallaItem = (EditText) findViewById(R.id.tvTalla);
        descripcionItem = (EditText) findViewById(R.id.tvDescripcion);
        imagenItem = (ImageView)findViewById(R.id.imagenIV);
        btnCancelarEdicion = (Button) findViewById(R.id.cancelarEdicion);
        btnConfirmarEdicion = (Button) findViewById(R.id.btnUpdateData);
        fab_editar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_editar);
        fab_eliminar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_eliminar);
        fab_compartir = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.fab_compartir);
        fab_menu = (com.getbase.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.fab_menu);
        layout = (FrameLayout) this.findViewById(android.R.id.content);

        nombreItem.setEnabled(false);
        tallaItem.setEnabled(false);
        descripcionItem.setEnabled(false);

        nombreItem.setText(DataHolder.instance.getNombreItem());
        tallaItem.setText(DataHolder.instance.getTallaItem());
        descripcionItem.setText(DataHolder.instance.getDescripcionItem());

        if (!DataHolder.instance.getImagenItem().equals("no image")){
            Uri fotoUri = Uri.parse(DataHolder.instance.getImagenItem());
            Picasso.with(this).load(fotoUri).into(imagenItem);
        } else {
            imagenItem.setImageResource(R.drawable.nopic);
        }



        imagenItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementoSeleccionadoActivity.this, FullscreenImageActivity.class);
                startActivity(intent);
            }
        });


        fab_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.collapse();
                nombreItem.setEnabled(true);
                tallaItem.setEnabled(true);
                descripcionItem.setEnabled(true);
                btnCancelarEdicion.setVisibility(View.VISIBLE);
                btnConfirmarEdicion.setVisibility(View.VISIBLE);
            }
        });

        fab_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri bmpUri = null;
                try {
                    bmpUri = getLocalBitmapUri(imagenItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bmpUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {
                }
            }
        });

        fab_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.collapse();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("¿Estás seguro de que quieres eliminar este elemento?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                deleteData();
                                Toast.makeText(ElementoSeleccionadoActivity.this, "Borrado", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });

                builder.setNegativeButton(
                        "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (fab_menu.isExpanded())
                    fab_menu.collapse();
                return true;
            }
        });

        btnCancelarEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancelarEdicion.setVisibility(View.INVISIBLE);
                btnConfirmarEdicion.setVisibility(View.INVISIBLE);
                nombreItem.setEnabled(false);
                tallaItem.setEnabled(false);
                descripcionItem.setEnabled(false);
            }
        });


        btnConfirmarEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancelarEdicion.setVisibility(View.INVISIBLE);
                btnConfirmarEdicion.setVisibility(View.INVISIBLE);
                nombreItem.setEnabled(false);
                tallaItem.setEnabled(false);
                descripcionItem.setEnabled(false);
                updateData(nombreItem.getText().toString(), tallaItem.getText().toString(), descripcionItem.getText().toString());
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void updateData(String nombre, String talla, String descripcion) {

        DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                child(Integer.toString(DataHolder.instance.getCategoria())).child("objetos")
                .child(Integer.toString(DataHolder.instance.getItemPosition())).child("nombre").setValue(nombre);

        DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                child(Integer.toString(DataHolder.instance.getCategoria())).child("objetos")
                .child(Integer.toString(DataHolder.instance.getItemPosition())).child("talla").setValue(talla);

        DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias").
                child(Integer.toString(DataHolder.instance.getCategoria())).child("objetos")
                .child(Integer.toString(DataHolder.instance.getItemPosition())).child("descripcion").setValue(descripcion);

        Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();

    }

    public void deleteData() {
        DataHolder.instance.mDatabase.child("usuarios").child(DataHolder.instance.firebaseUser.getUid()).child("categorias")
                .child(Integer.toString(DataHolder.instance.getCategoria())).child("objetos")
                .child(Integer.toString(DataHolder.instance.getItemPosition())).removeValue();
    }

    public Uri getLocalBitmapUri(ImageView imageView) throws IOException {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        Uri bmpUri = null;
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.close();
        bmpUri = Uri.fromFile(file);

        return bmpUri;
    }



}