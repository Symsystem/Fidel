package com.fidel.fidel.activities;

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

import com.android.volley.toolbox.StringRequest;
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


public class ConnectActivity extends ActionBarActivity {

    @InjectView(R.id.inscriptionButton) Button mInscrButton;
    @InjectView(R.id.connectLogin) EditText mConnectLogin;
    @InjectView(R.id.connectPassword) EditText mConnectPassword;
    @InjectView(R.id.connectButton) Button mConnectButton;
    @InjectView(R.id.textLogo) TextView mTextLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ButterKnife.inject(this);
    }

    @OnClick (R.id.connectButton)
    public void onClickConnectButton(){
        String login = mConnectLogin.getText().toString().trim();
        String password = mConnectPassword.getText().toString().trim();
        password = encrypt(password);
        if(login.isEmpty() || password.isEmpty()){
            Toast.makeText(this, R.string.emptyField,Toast.LENGTH_LONG).show();
        } else {
            String URL = Utils.BASE_URL + "api/connexion/" + login + "/" + password + ".json";

            StringRequest requestSignUp = new StringRequest(URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response") && userJSON.getBoolean("response")) {
                            if (userJSON.getBoolean("connexionOK")) {
                                Intent intent2 = new Intent(ConnectActivity.this,MainActivity.class);
                                startActivity(intent2);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
                                builder.setTitle("Erreur");
                                builder.setMessage("Mauvais mot de passe ou nom d'utilisateur");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
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
            RequestQueue queue = Volley.newRequestQueue(ConnectActivity.this, new OkHttpStack());
            queue.add(requestSignUp);
        }

    }

    @OnClick (R.id.inscriptionButton)
    public void onClickInscrButton(){
        Intent intent = new Intent(ConnectActivity.this, SignUpActivity.class);
        startActivity(intent);
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
