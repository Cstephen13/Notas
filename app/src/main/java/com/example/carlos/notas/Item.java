package com.example.carlos.notas;

import com.android.volley.toolbox.StringRequest;

/**
 * Created by Carlos on 6/01/2017.
 */

public class Item {

    private String nombreItem;
    private int id;
    private double nota;
    private String aux;

    public Item(){}
    public Item(String nombreItem,double nota){
        this.nombreItem=nombreItem;
        this.nota=nota;
    }


    public String getNombreItem() {
        return nombreItem;
    }

    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }
}
