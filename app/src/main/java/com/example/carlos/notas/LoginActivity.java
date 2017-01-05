package com.example.carlos.notas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
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
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    JsonObjectRequest array;
    RequestQueue mRequestQueue;
    ListAdapter adapter;
    private final String url = "http://192.168.0.105:8000/Sistema-Gestion-de-Notas/public/android/iniciarSesion";
    private final String TAG = "PRUEBITA";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mcodigo;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Estudiante e;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mcodigo = (EditText) findViewById(R.id.codigo);
        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mcodigo.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String codigoEs = mcodigo.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(codigoEs)) {
            mcodigo.setError(getString(R.string.error_field_required));
            focusView = mcodigo;
            cancel = true;
        }/* else if (!isEmailValid(codigoEs)) {
            codigo.setError(getString(R.string.error_invalid_email));
            focusView = codigo;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(codigoEs, password);
            mAuthTask.execute((Void) null);
        }
    }

    /*private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }*/

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mcodigo;
        private final String mPassword;

        UserLoginTask(String codigo, String password) {
            mcodigo = codigo;
            mPassword = password;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try{
                Thread.sleep(5000);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
            StringRequest request= new StringRequest(Request.Method.POST,Conexion.INICIAR_SESION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        Log.d("maxtoken",response);
                        try {
                            JSONObject obj= new JSONObject(response);
                            JSONObject estudianteJson= new JSONObject(new  JSONArray(obj.getString("estudiante")).get(0).toString());

                             e = new Estudiante(
                                    estudianteJson.getString("primerNombre"),
                                    estudianteJson.getString("segundoNombre"),
                                    estudianteJson.getString("primerApellido"),
                                    estudianteJson.getString("segundoApellido"),
                                    estudianteJson.getString("email"),
                                    estudianteJson.getString("codigo"),
                                    Boolean.parseBoolean(estudianteJson.getString("estado")),
                                    Integer.parseInt(estudianteJson.getString("id")),
                                    obj.getString("token")
                                    );
                            Sesion.setEstudiante(e);
                            Intent intent = new Intent(getApplicationContext(),Principal.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"iniciaste sesion papu",Toast.LENGTH_SHORT).show();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Log.d("elarta", "error al tratar de sacar los objetos json");
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null){
                        if (error.networkResponse.statusCode == 401){
                            Toast.makeText(getApplicationContext(),"Código o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        }else if(error.networkResponse.statusCode == 500){
                            Toast.makeText(getApplicationContext(),"No se ha enviado el token",Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("codigo",mcodigo);
                    map.put("password",mPassword);

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

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

