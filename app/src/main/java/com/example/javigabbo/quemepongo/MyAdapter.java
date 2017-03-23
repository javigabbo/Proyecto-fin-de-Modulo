package com.example.javigabbo.quemepongo;

/**
 * Created by Javigabbo on 2/2/17.
 */

import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jonathan.barmagen on 02/02/2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Objeto> mDataset;
    public static RecyclerViewMyAdapterClickListener itemListener;
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Objeto> myDataset,RecyclerViewMyAdapterClickListener itemListener) {
        if(myDataset == null){
            mDataset = new ArrayList<Objeto>();
        }else{
            mDataset = myDataset;
        }

        this.itemListener=itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView ll = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.celda, parent, false);


        //TextView nombre=(TextView) ll.findViewById(R.id.txtdatos);
        // set the view's size, margins, paddings and layout parameters
        //ViewGroup.LayoutParams params = ll.getLayoutParams();
        //params.height=500;
        //ll.setLayoutParams(params);

        ViewHolder vh = new ViewHolder(ll);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v("MENU", mDataset.get(position)     + "");
        holder.textView.setText(mDataset.get(position).getNombre().toString());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView ll;
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(CardView ll) {
            super(ll);
            this.ll = ll;
            textView=(TextView) ll.findViewById(R.id.info_text);
            imageView=(ImageView) ll.findViewById(R.id.imgcelda);

            ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println("PASA POR ONCLICK DEL ADAPTER");
            itemListener.recyclerViewMyAdapterListClicked(v, this.getLayoutPosition());
        }
    }


    public interface RecyclerViewMyAdapterClickListener{
        public void recyclerViewMyAdapterListClicked(View v, int position);
    }
}