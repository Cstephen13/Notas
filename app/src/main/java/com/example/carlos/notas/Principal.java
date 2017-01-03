package com.example.carlos.notas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Principal extends AppCompatActivity {
    Sesion s= new Sesion();
    private final Estudiante e = s.getEstudianteEnSesion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);
        Button salir = (Button) findViewById(R.id.btnsalir);
        TextView nombreEstudiante = (TextView) findViewById(R.id.nombreEstudiante);
        //nombreEstudiante.setText(e.getPrimerNombre()+e.getPrimerApellido());
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   Log.d("nombre",e.getPrimerNombre());
               }catch (Exception e1){

                   Log.d("alerta","algo paso el estudiante esta vacio");
               }
            }
        });

    }
}
