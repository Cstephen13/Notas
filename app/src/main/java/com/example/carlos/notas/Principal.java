package com.example.carlos.notas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {
    Sesion s;
    ListView lista;
    RequestQueue mRequestQueue;
    ArrayList<Item> asignaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        s = new Sesion();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);
        Button salir = (Button) findViewById(R.id.btnsalir);
        TextView nombreEstudiante = (TextView) findViewById(R.id.nombreEstudiante);

        lista = (ListView) findViewById(R.id.listaAsignaturas);
        try {
            nombreEstudiante.setText(s.getEstudianteEnSesion().getPrimerNombre()+" "+s.getEstudianteEnSesion().getPrimerApellido());
            Log.d("el token", s.getEstudianteEnSesion().getToken());
        }catch (Exception e1){

            Log.d("alerta",e1.toString());
        }
        final CargarLista c = new CargarLista(this);
        c.execute();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),  asignaturas.get(posicion).getNombreItem(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),Notas.class);
                intent.putExtra("matricula_id", Integer.toString(asignaturas.get(posicion).getId()));
                intent.putExtra("nombreAsignatura", asignaturas.get(posicion).getNombreItem());
                startActivity(intent);
            }

        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //s.setEstudiante(null);
                finish();
               try {
                   Log.d("nombre",Sesion.getEstudianteEnSesion().getPrimerNombre());
               }catch (Exception e1){

                   Log.d("alerta",e1.toString());
               }
            }
        });

    }


    public class CargarLista extends AsyncTask<Void,Void,MiAdaptador>{
        Context context;
        ProgressDialog pDialog;
        MiAdaptador adaptador;
        ArrayList<String> asig = new ArrayList<>();

        Item as;


        public CargarLista(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Cargando Lista");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected MiAdaptador doInBackground(Void... params) {

            try{
                Thread.sleep(4000);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    Conexion.URL_LISTAR_NOTAS+"?token="+s.getEstudianteEnSesion().getToken()+"&id="+s.getEstudianteEnSesion().getId(),

                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("asignaturas", response.toString());
                    try {
                        JSONObject obj= new JSONObject(response);
                        JSONArray b = new JSONArray(obj.getString("asignaturasUltimoPeriodo"));

                        asignaturas = new ArrayList<>();
                        for (int i=0;i<response.length();i++){
                            as = new Item();
                            as.setId(Integer.parseInt(b.getJSONObject(i).getString("matricula_id")));
                            as.setNombreItem(b.getJSONObject(i).getString("nombre_asignatura"));
                            as.setNota(Double.parseDouble(b.getJSONObject(i).getString("definitiva")));
                            asignaturas.add(as);

                            Log.d("asignaturas_nombre",as.getNombreItem());
                            asig.add(b.getJSONObject(i).getString("nombre_asignatura"));

                            Log.d("asignaturas",b.getJSONObject(i).getString("nombre_asignatura"));


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    adaptador = new MiAdaptador(context, asignaturas);
                   // adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, asig);
                    lista.setAdapter(adaptador);
                    adaptador.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("algo pasa", error.toString());
                }
            })
            {
                @Override
                public RetryPolicy getRetryPolicy() {
                    return new DefaultRetryPolicy(
                            5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                }
            };
            mRequestQueue.add(request);


            return adaptador;
        }

        @Override
        protected void onPostExecute(MiAdaptador result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            lista.setAdapter(result);
            pDialog.dismiss();
        }

    }
}
