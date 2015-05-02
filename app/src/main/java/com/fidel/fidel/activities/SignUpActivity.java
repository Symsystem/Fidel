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
        String login = mUserLogin.getText().toString().trim();
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
