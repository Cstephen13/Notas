package com.example.carlos.notas;

/**
 * Created by Carlos on 27/12/2016.
 */

public class Sesion {

    public  static Estudiante EstudianteEnSesion;



    public  static void setEstudiante(Estudiante e){
         EstudianteEnSesion = e;
    }

    public static  Estudiante getEstudianteEnSesion(){

        return EstudianteEnSesion;
    }


}
