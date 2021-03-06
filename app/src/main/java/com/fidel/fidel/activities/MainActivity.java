package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    //Cette activité permet à l'utilisateur de se renseigner sur les différentes réglementations lorsqu'il prépare ses bagages.
    //Il peut consulter les tailles autorisées des bagages, leur poids, ainsi que la liste des objets interdits.
    //C'est dans cette activité que l'utilsateur entre son numéro de réservation lorsqu'il arrive à l'aéroport, pour lancer la procédure.

    Toolbar toolbar;

    @InjectView(R.id.numResLayout) LinearLayout mNumResLayout;
    @InjectView(R.id.startButton) Button mStartButton;
    @InjectView(R.id.okNumRes) Button mOkNumRes;
    @InjectView(R.id.editNumRes) EditText mNumRes;
    @InjectView(R.id.listButton) ImageButton mListButton;
    @InjectView(R.id.userButton) ImageButton mUserButton;
    @InjectView(R.id.weightLuggageButton) ImageButton mWeihtLuggageButton;
    @InjectView(R.id.sizeLuggageButton) ImageButton mSizeLuggageButton;
    @InjectView(R.id.logId) TextView mLogId;
    @InjectView(R.id.disconnectButton) Button mDisconnectButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

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

        if(mProgressBar.getVisibility() == ProgressBar.VISIBLE){
            mProgressBar.setVisibility(ProgressBar.GONE);
        }

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user"); //On récupère les données de l'utilisateur depuis la ConnectActivity
        mLogId.setText(getResources().getString(R.string.welcom) + " " + user.getLogin() + "!");

        mNumResLayout.setVisibility(View.INVISIBLE);

        animUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        animDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);


        // Made visible the booking number textfield and valid button
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNumResLayout.getVisibility() == View.INVISIBLE){
                    mStartButton.setText(R.string.cancel);
                    mNumResLayout.setVisibility(View.VISIBLE);
                    mNumResLayout.startAnimation(animDown);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mNumRes, InputMethodManager.SHOW_IMPLICIT);
                }
                else{
                    mStartButton.setText(R.string.start);
                    mNumResLayout.startAnimation(animUp);
                    mNumResLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @OnClick (R.id.okNumRes)
    public void onClickOkNumRes(){
        String numRes = mNumRes.getText().toString().trim(); //On récupère le numéro de réservation entré
        if(numRes.isEmpty()){ //On vérifie qu'il y a bien quelque chose dans le champ
            Toast.makeText(this, R.string.emptyNumVol, Toast.LENGTH_LONG).show();
        } else {
            String URL = Utils.BASE_URL + "api/reservations/" + numRes + "/logins/" + user.getId() + ".json";

            StringRequest requestSendNumRes = new StringRequest(URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    Reservation mReservation = new Reservation();

                    try{
                        JSONObject jsonReservation = new JSONObject(s);
                        if (jsonReservation.has("response")) { //On vérifie que le serveur répond
                            if (jsonReservation.getInt("response")==Utils.SUCCESS) { //On vérifie que le compte en question est bien lié au numéro de réservation entré
                                //On récupère toutes les informations nécessaires concernant la réservation depuis le serveur
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

                                    Personne pers = new Personne(
                                        jsonPers.getInt("id"),
                                        jsonPers.getString("name"),
                                        jsonPers.getString("firstName"),
                                        jsonPers.getString("address"),
                                        jsonPers.getString("locality"),
                                        jsonPers.getInt("postcode"),
                                        jsonPers.getString("country"),
                                        jsonPers.getString("phone"),
                                        jsonPers.getString("birth"),
                                        jsonPers.getString("passeportDate"));

                                    mReservation.addPers(pers);
                                }
                                JSONArray arraySiege = jsonReservation.getJSONArray("sieges");
                                for(int i = 0; i < arraySiege.length();i++){
                                    JSONObject jsonObject = arraySiege.getJSONObject(i);

                                    mReservation.addNumSiege(jsonObject.getInt("idPersonne"),jsonObject.getString("place"));
                                }
                                JSONObject jsonUser = jsonReservation.getJSONObject("user");
                                User user = new User(jsonUser.getInt("id"),
                                        jsonUser.getString("login"),
                                        jsonUser.getString("email"),
                                        jsonUser.getInt("wallet"));
                                mReservation.setUser(user);
                                mReservation.setNumLuggages(jsonReservation.getInt("nbrLuggages"));

                                Intent intent = new Intent(MainActivity.this, ProcessActivity.class);
                                intent.putExtra("reservation", mReservation); //On envoie les données de la réservation à l'activité suivante
                                mProgressBar.setVisibility(ProgressBar.GONE);
                                startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Erreur");
                                builder.setMessage("Ce numéro de réservation n'est pas lié à votre compte");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                mProgressBar.setVisibility(ProgressBar.GONE);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès au serveur, veuillez patienter");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mProgressBar.setVisibility(ProgressBar.GONE);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès Internet, veuillez l'activer");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mProgressBar.setVisibility(ProgressBar.GONE);
                            Log.e("errorConnexion", volleyError.getMessage());
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this, new OkHttpStack());
            queue.add(requestSendNumRes);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
    }

    @OnClick (R.id.listButton) //Bouton permettant d'accéder à la liste des objets interdits
    public void onClickListButton(){
        Intent intent = new Intent(MainActivity.this, ForbiddenListActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.userButton) //Boutin permettant d'accéder aux informations de l'utilisateur
    public void onClickUserButton(){
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @OnClick (R.id.weightLuggageButton) //Bouton permettant d'accéder aux limitations de poids des bagages
    public void onClickWeightLuggageButton(){
        Intent intent = new Intent(MainActivity.this, WeightLuggageActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.sizeLuggageButton) //Boutin permetant d'accéder aux limitations de taille des bagages
    public void onClickSizeLuggageButton(){
        Intent intent = new Intent(MainActivity.this, SizeLuggageActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.disconnectButton) //Bouton permettant de se déconnecter de l'application
    public void onClickDisconnectButton(){
        Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
        startActivity(intent);
    }

}
