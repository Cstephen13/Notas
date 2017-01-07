package com.example.carlos.notas;

/**
 * Created by Carlos on 6/01/2017.
 */

public class Asignatura {

    private String nombre;
    private int id;

    public Asignatura(){}
    public Asignatura(String nombre, int id){
        this.nombre = nombre;
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
