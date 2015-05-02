package com.fidel.fidel;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ConnectActivity extends ActionBarActivity {

    @InjectView(R.id.inscriptionButton) Button mInscrButton;
    @InjectView(R.id.connectLogin) EditText mConnectLogin;
    @InjectView(R.id.connectPassword) EditText mConnectPassword;
    @InjectView(R.id.connectButton) Button mConnectButton;

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
        if(login.isEmpty() || password.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
            builder.setTitle("Erreur");
            builder.setMessage("Vous n'avez pas rempli tous les champs");
            builder.setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            params.put("loggin", login);
            params.put("pasword", password);
            String URL = "http://fidel.symsystem.com/" + //A COMPLETER

            PostRequest requestSignUp = new PostRequest(URL, params, new Response.Listener<String>()){
                @Override
                public void onResponse(String s){
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if (userJSON.getBoolean("response")) {
                                Intent intent2 = new Intent(ConnectActivity.this,StartProcessActivity.class);
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
            };

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

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
}
