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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {
    Sesion s;
    ListView lista;
    RequestQueue mRequestQueue;

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
        }catch (Exception e1){

            Log.d("alerta",e1.toString());
        }
        final CargarLista c = new CargarLista(this);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Ha pulsado el elemento " + posicion, Toast.LENGTH_SHORT).show();
            }

        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //s.setEstudiante(null);
                //finish();

                c.execute();
               try {
                   Log.d("nombre",Sesion.getEstudianteEnSesion().getPrimerNombre());
               }catch (Exception e1){

                   Log.d("alerta",e1.toString());
               }
            }
        });

    }


    public class CargarLista extends AsyncTask<Void,Void,ArrayAdapter<String>>{
        Context context;
        ProgressDialog pDialog;
        ArrayAdapter<String> adaptador;

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
        protected ArrayAdapter<String> doInBackground(Void... params) {

            try{
                Thread.sleep(2000);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            /*JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            Log.d("Response", response.toString());
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", response);
                        }
                    }
            );*/


            mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
            StringRequest request= new StringRequest(Request.Method.GET,Conexion.URL_LISTAR_NOTAS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("maxtoken",response);
                     adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Integer.parseInt(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 401) {
                            Toast.makeText(getApplicationContext(), "Código o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "No se ha enviado el token", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("id",Integer.toString(s.getEstudianteEnSesion().getId()));
                    map.put("token",s.getEstudianteEnSesion().getToken());
                    return map;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }

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
        protected void onPostExecute(ArrayAdapter<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            lista.setAdapter(result);
            pDialog.dismiss();
        }

    }
}
