package com.example.javigabbo.quemepongo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Javigabbo on 31/1/17.
 */

public class AdapterCategorias extends BaseAdapter {


    ArrayList<Objeto> objetos;

    public AdapterCategorias(ArrayList<Objeto> objetos) {
        this.objetos = objetos;
    }

    @Override
    public int getCount() {
        return objetos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
