package com.fidel.fidel.activities;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.request.OkHttpStack;
import com.fidel.fidel.request.PostRequest;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
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
        password = encrypt(password);
        String passwordBis = mUserPasswordBis.getText().toString().trim();
        passwordBis = encrypt(passwordBis);
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
                String URL = Utils.BASE_URL + "api/register" + ".json";

                PostRequest requestRegister = new PostRequest(URL, params, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s){
                        try {
                            JSONObject userJSON = new JSONObject(s);
                            if (userJSON.has("response") && userJSON.getInt("response")==Utils.SUCCESS) {
                                if (userJSON.getBoolean("numResValidity")) {
                                    Intent intent2 = new Intent(SignUpActivity.this,MainActivity.class);
                                    intent2.putExtra("login", login);
                                    startActivity(intent2);
                                }
                                switch(userJSON.getInt("response")){
                                    case Utils.NUM_RES_KO:  AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                                            builder.setTitle("Erreur");
                                                            builder.setMessage("Votre numéro de réservation n'est plus valide");
                                                            builder.setPositiveButton(android.R.string.ok, null);
                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();
                                                            break;
                                    case Utils.LOGIN_TAKEN: AlertDialog.Builder builderbis = new AlertDialog.Builder(SignUpActivity.this);
                                                            builderbis.setTitle("Erreur");
                                                            builderbis.setMessage("Ce nom d'utilisateur est déjà utilisé");
                                                            builderbis.setPositiveButton(android.R.string.ok, null);
                                                            AlertDialog dialogbis = builderbis.create();
                                                            dialogbis.show();
                                                            break;
                                    case Utils.EMAIL_TAKEN: AlertDialog.Builder builderbisbis = new AlertDialog.Builder(SignUpActivity.this);
                                                            builderbisbis.setTitle("Erreur");
                                                            builderbisbis.setMessage("Cette adresse email est déjà enregistrée");
                                                            builderbisbis.setPositiveButton(android.R.string.ok, null);
                                                            AlertDialog dialogbisbis = builderbisbis.create();
                                                            dialogbisbis.show();
                                                            break;
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setTitle("Erreur");
                                builder.setMessage("Pas d'accès au serveur, veuillez patienter");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
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
