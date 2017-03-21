package com.example.javigabbo.quemepongo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class ElementoSeleccionadoActivity extends AppCompatActivity {

    EditText nombreItem;
    EditText tallaItem;
    ImageView imagenItem;
    Button btnCancelarEdicion;
    Button btnConfirmarEdicion;
    FrameLayout layout;
    com.getbase.floatingactionbutton.FloatingActionButton fab_editar;
    com.getbase.floatingactionbutton.FloatingActionButton fab_eliminar;
    com.getbase.floatingactionbutton.FloatingActionsMenu fab_menu;
    Objeto objeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elemento_seleccionado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(DataHolder.instance.getNombreItem());
        //objeto


        nombreItem = (EditText)findViewById(R.id.tvNombre);
        tallaItem = (EditText)findViewById(R.id.tvTalla);
        btnCancelarEdicion = (Button)findViewById(R.id.cancelarEdicion);
        btnConfirmarEdicion = (Button)findViewById(R.id.btnUpdateData);
        fab_editar = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.fab_editar);
        fab_eliminar = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.fab_eliminar);
        fab_menu = (com.getbase.floatingactionbutton.FloatingActionsMenu)findViewById(R.id.fab_menu);
        layout = (FrameLayout) this.findViewById(android.R.id.content);

        nombreItem.setEnabled(false);
        tallaItem.setEnabled(false);

        nombreItem.setText(DataHolder.instance.getNombreItem());
        tallaItem.setText(DataHolder.instance.getTallaItem());

        fab_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.collapse();
                nombreItem.setEnabled(true);
                tallaItem.setEnabled(true);
                btnCancelarEdicion.setVisibility(View.VISIBLE);
                btnConfirmarEdicion.setVisibility(View.VISIBLE);
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(fab_menu.isExpanded())
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
            }
        });


        btnConfirmarEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancelarEdicion.setVisibility(View.INVISIBLE);
                btnConfirmarEdicion.setVisibility(View.INVISIBLE);
                nombreItem.setEnabled(false);
                tallaItem.setEnabled(false);
                updateData(nombreItem.getText().toString(), tallaItem.getText().toString());
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void updateData(String nombre, String talla){
        //DataHolder.instance.mDatabase.child()
    }


}
