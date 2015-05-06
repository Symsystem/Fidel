package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
import com.fidel.fidel.classes.Personne;
import com.fidel.fidel.classes.Reservation;
import com.fidel.fidel.classes.User;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.classes.Vol;
import com.fidel.fidel.enums.TypeVol;
import com.fidel.fidel.enums.TypeVoyageur;
import com.fidel.fidel.request.OkHttpStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;

    @InjectView(R.id.numVolLayout) LinearLayout mNumVolLayout;
    @InjectView(R.id.startButton) Button mStartButton;
    @InjectView(R.id.okNumRes) Button mOkNumRes;
    @InjectView(R.id.editNumVol) EditText mNumvol;
    @InjectView(R.id.listButton) ImageButton mListButton;
    @InjectView(R.id.userButton) ImageButton mUserButton;
    @InjectView(R.id.weightLuggageButton) ImageButton mWeihtLuggageButton;
    @InjectView(R.id.sizeLuggageButton) ImageButton mSizeLuggageButton;
    @InjectView(R.id.logId) TextView mLogId;
    @InjectView(R.id.disconnectButton) Button mDisconnectButton;

    private Animation animUp, animDown;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        mLogId.setText(getResources().getString(R.string.welcom) + " " + user.getLogin() + "!");

        mNumVolLayout.setVisibility(View.INVISIBLE);

        animUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        animDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);


        // Made visible the flight number textfield and valid button
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNumVolLayout.getVisibility() == View.INVISIBLE){
                    mStartButton.setText(R.string.cancel);
                    mNumVolLayout.setVisibility(View.VISIBLE);
                    mNumVolLayout.startAnimation(animDown);
                }
                else{
                    mStartButton.setText(R.string.start);
                    mNumVolLayout.startAnimation(animUp);
                    mNumVolLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @OnClick (R.id.okNumRes)
    public void onClickOkNumRes(){
        String numRes = mNumvol.getText().toString().trim();
        if(numRes.isEmpty()){
            Toast.makeText(this, R.string.emptyNumVol, Toast.LENGTH_LONG).show();
        } else {
            String URL = Utils.BASE_URL + "api/reservations/" + numRes + "/logins/" + user.getLogin() + ".json";

            StringRequest requestSendNumRes = new StringRequest(URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    Reservation mReservation = new Reservation();
                    try{
                        JSONObject jsonReservation = new JSONObject(s);
                        if (jsonReservation.has("response")) {
                            if (jsonReservation.getInt("response")==Utils.SUCCESS) {
                                mReservation.setId(jsonReservation.getInt("id"));
                                mReservation.setNumRes(jsonReservation.getString("numRes"));
                                mReservation.setDate(jsonReservation.getString("date"));
                                mReservation.setTypeVoyageur(jsonReservation.getBoolean("typeVoyageur")? TypeVoyageur.TOURISTE : TypeVoyageur.AFFAIRE);

                                JSONObject jsonVol = jsonReservation.getJSONObject("vol");
                                Vol vol = new Vol();
                                vol.setId(jsonVol.getInt("id"));
                                vol.setNumVol(jsonVol.getString("numVol"));
                                vol.setTypeVol(jsonVol.getBoolean("typeVol") ? TypeVol.DOMESTIQUE : TypeVol.INTERNATIONAL);
                                vol.setDepart(jsonVol.getString("depart"));
                                vol.setArrivee(jsonVol.getString("arrivee"));
                                vol.setHeureArrivee(jsonVol.getString("hDepart"));
                                vol.setHeureDepart(jsonVol.getString("hArrivee"));
                                vol.setHeureEmbarquement(jsonVol.getString("hEmbarquement"));
                                vol.setGate(jsonVol.getString("gate"));

                                mReservation.setVol(vol);

                                JSONArray arrayPersonnes = jsonReservation.getJSONArray("personnes");
                                for(int i = 0; i < arrayPersonnes.length(); i++){
                                    JSONObject jsonPers = arrayPersonnes.getJSONObject(i);

                                    Personne pers = new Personne();
                                    pers.setId(jsonPers.getInt("id"));
                                    pers.setNom(jsonPers.getString("name"));
                                    pers.setPrenom(jsonPers.getString("firstName"));
                                    pers.setAddress(jsonPers.getString("adsress"));
                                    pers.setLocality(jsonPers.getString("locality"));
                                    pers.setPostCode(jsonPers.getInt("postcode"));
                                    pers.setCountry(jsonPers.getString("country"));
                                    pers.setNumPhone(jsonPers.getString("phone"));
                                    pers.setBirthDate(jsonPers.getString("birth"));
                                    pers.setPasseportValidity(jsonPers.getString("passeportDate"));

                                    mReservation.getListPersonne().add(pers);
                                }
                                JSONObject jsonUser = jsonReservation.getJSONObject("user");
                                User user = new User(jsonUser.getInt("id"),
                                        jsonUser.getString("login"),
                                        jsonUser.getString("email"),
                                        jsonUser.getInt("wallet"));
                                Intent intent = new Intent(MainActivity.this, ProcessActivity.class);
                                intent.putExtra("reservation", mReservation);
                                startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Erreur");
                                builder.setMessage("Ce numéro de réservation n'est pas lié à votre compte");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this, new OkHttpStack());
            queue.add(requestSendNumRes);
        }
    }

    @OnClick (R.id.listButton)
    public void onClickListButton(){
        Intent intent = new Intent(MainActivity.this, ForbiddenListActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.userButton)
    public void onClickUserButton(){
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @OnClick (R.id.weightLuggageButton)
    public void onClickWeightLuggageButton(){
        Intent intent = new Intent(MainActivity.this, WeightLuggageActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.sizeLuggageButton)
    public void onClickSizeLuggageButton(){
        Intent intent = new Intent(MainActivity.this, SizeLuggageActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.disconnectButton)
    public void onClickDisconnectButton(){
        Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
        startActivity(intent);
    }

}
