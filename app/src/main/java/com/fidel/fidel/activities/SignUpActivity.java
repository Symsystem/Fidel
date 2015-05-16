package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.request.OkHttpStack;
import com.fidel.fidel.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SignUpActivity extends ActionBarActivity {

    Toolbar toolbar;

    @InjectView(R.id.ValiderButton) Button mValiderButton;
    @InjectView(R.id.userLogin) EditText mUserLogin;
    @InjectView(R.id.userPassword) EditText mUserPassword;
    @InjectView(R.id.userPasswordBis) EditText mUserPasswordBis;
    @InjectView(R.id.userNumRes) EditText mUserNumRes;
    @InjectView(R.id.userEmail) EditText mUserEmail;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ButterKnife.inject(this);
    }

    @OnClick (R.id.ValiderButton)
    public void onClickValiderButton(){
        final String login = mUserLogin.getText().toString().trim();
        String password = mUserPassword.getText().toString().trim();
        //password = encrypt(password);
        String passwordBis = mUserPasswordBis.getText().toString().trim();
        //passwordBis = encrypt(passwordBis);
        String numRes = mUserNumRes.getText().toString().trim();
        String email = mUserEmail.getText().toString().trim();

        if(login.isEmpty() || password.isEmpty() || passwordBis.isEmpty() || numRes.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, R.string.emptyField, Toast.LENGTH_LONG).show();
        } else if(!(password.equals(passwordBis))) {
            Toast.makeText(this, R.string.passwordNotEquals, Toast.LENGTH_LONG).show();
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("login", login);
            params.put("password",password);
            params.put("numRes", numRes);
            params.put("email", email);
            String URL = Utils.BASE_URL + "api/users" + ".json";

            PostRequest requestRegister = new PostRequest(URL, params, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if (userJSON.getInt("response") == Utils.SUCCESS) {
                                Intent intent2 = new Intent(SignUpActivity.this, ConnectActivity.class);
                                intent2.putExtra("login", login);
                                startActivity(intent2);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setPositiveButton(android.R.string.ok, null);
                                builder.setTitle("Erreur");

                                switch (userJSON.getInt("response")) {
                                    case Utils.NUM_RES_KO:
                                        builder.setMessage("Votre numéro de réservation n'est plus valide");
                                        break;
                                    case Utils.LOGIN_TAKEN:
                                        builder.setMessage("Ce nom d'utilisateur est déjà utilisé");
                                        break;
                                    case Utils.EMAIL_TAKEN:
                                        builder.setMessage("Cette adresse email est déjà enregistrée");
                                        break;
                                }

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès au serveur, veuillez patienter");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("errorConnexion", volleyError.getMessage());
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this, new OkHttpStack());
            queue.add(requestRegister);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }

    public String encrypt(String password){
        String crypte="";
        for (int i=0; i<password.length();i++)  {
            int c=password.charAt(i)^48;
            crypte=crypte+(char)c;
        }
        return crypte;
    }




}
