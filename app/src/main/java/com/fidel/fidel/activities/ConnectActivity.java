package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
import com.fidel.fidel.classes.User;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.request.OkHttpStack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ConnectActivity extends ActionBarActivity {

    //Cette activité permet à un utilisateur de se connecter à l'application, et donc de récupérer des données depuis le serveur.

    @InjectView(R.id.inscriptionButton) Button mInscrButton;
    @InjectView(R.id.connectLogin) EditText mConnectLogin;
    @InjectView(R.id.connectPassword) EditText mConnectPassword;
    @InjectView(R.id.connectButton) Button mConnectButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ButterKnife.inject(this);

        if(mProgressBar.getVisibility() == ProgressBar.VISIBLE){
            mProgressBar.setVisibility(ProgressBar.GONE);
        }

        //Si l'utilisateur vient de s'inscrire, le nom d'utilisateur qu'il a entré dans la "SignUpActivity" est
        // automatiquement entré dans la case "nom d'utilisateur" de cette activité.
        Intent intent = getIntent();
        String newLogin = (String)intent.getStringExtra("login");
        if(newLogin != null){
            mConnectLogin.setText(newLogin);
        }
    }

    @OnClick (R.id.connectButton)
    public void onClickConnectButton(){
        final String login = mConnectLogin.getText().toString().trim(); //On récupère le nom d'utilisateur
        String password = mConnectPassword.getText().toString().trim(); //On récupère le mot de passe
        password = encrypt(password);
        if(login.isEmpty() || password.isEmpty()){
            Toast.makeText(this, R.string.emptyField,Toast.LENGTH_LONG).show(); //On vérifie si les deux champs sont remplis
        } else {
            String URL = Utils.BASE_URL + "api/logins/" + login + "/passwords/" + password + ".json";

            StringRequest requestSignUp = new StringRequest(URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        User user;
                        if (userJSON.has("response")) { //On vérifie d'abord si le serveur répond
                            if ((userJSON.getInt("response") == Utils.SUCCESS)) { //Ensuite on vérifie si le nom d'utilisateur correspond au mot de passe
                                user = new User(userJSON.getInt("id"), //On récupère les informations du compte de l'utilisateur
                                        userJSON.getString("login"),
                                        userJSON.getString("email"),
                                        userJSON.getInt("wallet"));
                                Intent intent2 = new Intent(ConnectActivity.this, MainActivity.class);
                                intent2.putExtra("user", user); //On envoie ces informations à l'activité suivante
                                mProgressBar.setVisibility(ProgressBar.GONE);
                                startActivity(intent2); //Si tout est OK on passe à l'activité suivante
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
                                builder.setTitle("Erreur");
                                builder.setMessage("Mauvais mot de passe ou nom d'utilisateur");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                mProgressBar.setVisibility(ProgressBar.GONE);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès au serveur, veuillez patienter");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
                    builder.setTitle("Erreur");
                    builder.setMessage("Pas d'accès Internet, veuillez l'activer");
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    Log.e("errorConnexion", volleyError.getMessage());
                }
            });
            RequestQueue queue = Volley.newRequestQueue(ConnectActivity.this, new OkHttpStack());
            queue.add(requestSignUp);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }

    @OnClick (R.id.inscriptionButton) //Cliquer sur ce bouton envoie l'utilisateur vers l'activité d'inscription
    public void onClickInscrButton(){
        Intent intent = new Intent(ConnectActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public String encrypt(String password){

        //Méthode qui nous permet de crypter le mot de passe avant de l'envoyer au serveur

        String crypte="";
        for (int i=0; i<password.length();i++)  {
            int c=password.charAt(i)^48;
            crypte=crypte+(char)c;
        }
        return crypte;
    }


}
