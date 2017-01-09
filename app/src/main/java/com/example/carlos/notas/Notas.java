package com.example.carlos.notas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;

public class Notas extends AppCompatActivity {
    int id;
    String nombreAsignatura;
    RequestQueue mRequestQueue;
    ArrayList<Item> asignaturas;
    //elementos de vista

    ListView lista;
    TextView nombreAsig;
    TextView sinResultados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        Intent intent = this.getIntent();

        lista = (ListView) findViewById(R.id.listaItems);
        nombreAsig =(TextView) findViewById(R.id.nombreAsignatura);
        sinResultados = (TextView) findViewById(R.id.sinResultados);

        try{
            final NotasItems nI = new NotasItems(this);

            id = Integer.parseInt(intent.getStringExtra("matricula_id"));
            nombreAsignatura=intent.getStringExtra("nombreAsignatura");
            nombreAsig.setText(nombreAsignatura);


            Log.d("nombreAsignatura",nombreAsignatura);
            Log.d("este es el id papu",Integer.toString(id));
            //lista = (ListView) findViewById(R.id.listaItems);
            nI.execute();
        }catch (Exception e){

            Log.d("ha pasado algo",e.toString());
        }


    }





    public class NotasItems extends AsyncTask<Void,Void,MiAdaptador>{
        Context context;
        ProgressDialog pDialog;
        MiAdaptador adaptador;
        ArrayList<String> asig = new ArrayList<>();

        Item as;

        public NotasItems(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Cargando items");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected MiAdaptador doInBackground(Void... params) {
            try{
                Thread.sleep(3000);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    Conexion.URL_LISTAR_ITEMS+"?token="+Sesion.getEstudianteEnSesion().getToken()+"&matricula="+id,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            asignaturas = new ArrayList<>();
                            Log.d("asignaturas", response.toString());
                            try {
                                JSONObject obj= new JSONObject(response);
                                JSONArray b = new JSONArray(obj.getString("notas_matricula"));
                                if (b.length()==0){
                                    sinResultados.setText("No hay items registrados para esta asignatura");
                                    Log.d("alerta","esto esta mas vacio ");
                                }else{
                                    for (int i=0;i<response.length();i++){
                                        as = new Item();
                                        as.setNombreItem(b.getJSONObject(i).getString("nombre_item"));
                                        try{
                                            as.setNota(Double.parseDouble(b.getJSONObject(i).getString("nota")));
                                        }catch (Exception e){
                                            as.setAux(b.getJSONObject(i).getString("nota"));
                                        }

                                        as.setId(Integer.parseInt(b.getJSONObject(i).getString("item_id")));
                                        asignaturas.add(as);

                                        Log.d("items_nombre",as.getNombreItem());
                                        asig.add(b.getJSONObject(i).getString("nombre_item"));

                                        Log.d("items_nombre",b.getJSONObject(i).getString("nombre_item"));


                                    }
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
