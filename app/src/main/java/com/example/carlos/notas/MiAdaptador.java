package com.example.carlos.notas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Carlos on 6/01/2017.
 */

public class MiAdaptador extends BaseAdapter {

    protected Context activity;
    protected ArrayList<Item>items;

    public MiAdaptador(){}
    public MiAdaptador(Context activity,ArrayList<Item>items){
        this.activity=activity;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {

        return items.get(position);
    }

    @Override
    public long getItemId(int position) {

        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView== null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inf.inflate(R.layout.custom_list,null);
        }
        Item asignaturas = items.get(position);

        TextView nombreItem=(TextView)v.findViewById(R.id.nombreItem);
        nombreItem.setText(asignaturas.getNombreItem());

        TextView detalle=(TextView)v.findViewById(R.id.detalleItem);
        detalle.setText(Double.toString(asignaturas.getNota()));


        return v;
    }
}
