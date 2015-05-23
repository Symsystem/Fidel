package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
import com.fidel.fidel.classes.User;
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

public class UserProfileActivity extends ActionBarActivity {

    Toolbar toolbar;

    private User user;

    @InjectView(R.id.getLogin) TextView mGetLogin;
    @InjectView(R.id.getEmail) TextView mGetEmail;
    @InjectView(R.id.getWallet) TextView mGetWallet;
    @InjectView(R.id.changeDataButton) Button mChangeDataButton;
    @InjectView(R.id.setLogin) EditText mSetLogin;
    @InjectView(R.id.setEmail) EditText mSetEmail;
    @InjectView(R.id.setPassword) EditText mSetPassword;
    @InjectView(R.id.setPasswordBis) EditText mSetPasswordBis;
    @InjectView(R.id.ValiderNewData) Button mValiderNewData;
    @InjectView(R.id.layout3) LinearLayout mLayout3;
    @InjectView(R.id.layout4) LinearLayout mLayout4;
    @InjectView(R.id.layout5) LinearLayout mLayout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        mGetLogin.setText(user.getLogin());
        mGetEmail.setText(user.getEmail());
        mGetWallet.setText("" + user.getWallet());
    }

    @OnClick (R.id.changeDataButton)
    public void onClickChangeDataButton(){
        mGetLogin.setVisibility(View.GONE);
        mGetEmail.setVisibility(View.GONE);
        mLayout3.setVisibility(View.GONE);
        mSetLogin.setVisibility(View.VISIBLE);
        mSetEmail.setVisibility(View.VISIBLE);
        mLayout4.setVisibility(View.VISIBLE);
        mLayout5.setVisibility(View.VISIBLE);
        mChangeDataButton.setVisibility(View.GONE);
        mValiderNewData.setVisibility(View.VISIBLE);
    }

    @OnClick (R.id.ValiderNewData)
    public void onClickValiderNewData(){
        final String login = mSetLogin.getText().toString().trim();
        final String email = mSetEmail.getText().toString().trim();
        String password = mSetPassword.getText().toString().trim();
        final String passwordBis = mSetPasswordBis.getText().toString().trim();

        if(login.isEmpty() && email.isEmpty() && password.isEmpty()){
            Toast.makeText(this, R.string.emptyOneField, Toast.LENGTH_LONG).show();
        }else if(!(password.equals(passwordBis))){
            Toast.makeText(this, R.string.passwordNotEquals, Toast.LENGTH_LONG).show();
        }else{
            Map<String,String> params = new HashMap<String,String>();
            params.put("userId",String.valueOf(user.getId()));
            params.put("login",login);
            params.put("email", email);
            params.put("password",password);

            String URL = Utils.BASE_URL + "api/users/profiles.json";

            PostRequest requestRegister = new PostRequest(URL, params, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if (userJSON.getInt("response") == Utils.SUCCESS) {
                                mGetLogin.setVisibility(View.VISIBLE);
                                mGetEmail.setVisibility(View.VISIBLE);
                                mLayout3.setVisibility(View.VISIBLE);
                                mSetLogin.setVisibility(View.GONE);
                                mSetEmail.setVisibility(View.GONE);
                                mLayout4.setVisibility(View.GONE);
                                mLayout5.setVisibility(View.GONE);
                                mChangeDataButton.setVisibility(View.VISIBLE);
                                mValiderNewData.setVisibility(View.GONE);
                                mGetLogin.setText(login);
                                mGetEmail.setText(email);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                                builder.setPositiveButton(android.R.string.ok, null);
                                builder.setTitle("Erreur");

                                switch (userJSON.getInt("response")) {
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès Internet, veuillez l'activer");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            Log.e("errorConnexion", volleyError.getMessage());
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(UserProfileActivity.this, new OkHttpStack());
            queue.add(requestRegister);
        }

    }

}
