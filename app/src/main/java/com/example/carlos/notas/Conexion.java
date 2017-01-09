package com.example.carlos.notas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Carlos on 29/12/2016.
 */

public class Conexion {

    public static String URL = "http://192.168.0.107:8000/Sistema-Gestion-de-Notas/public/android/";
    public static String INICIAR_SESION = URL + "iniciarSesion";
    public static String URL_LISTAR_NOTAS = URL + "notas";
    public static String URL_LISTAR_ITEMS= URL + "notasAsignatura";


    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }






}
