package com.example.carlos.notas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Notas extends AppCompatActivity {
    int id;

    //elementos de vista

    ListView lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("matricula_id"));
        lista = (ListView) findViewById(R.id.listaItems);



    }

    public class NotasItems extends AsyncTask<Void,Void,ArrayAdapter<String>>{
        Context context;
        ProgressDialog pDialog;
        ArrayAdapter<String> adaptador;

        public NotasItems(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
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

            return null;
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
