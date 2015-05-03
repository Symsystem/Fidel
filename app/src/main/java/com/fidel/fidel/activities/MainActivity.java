package com.fidel.fidel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.fidel.fidel.R;
import com.fidel.fidel.classes.Reservation;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.classes.Vol;
import com.fidel.fidel.enums.TypeVoyageur;
import com.fidel.fidel.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;

    @InjectView(R.id.numVolLayout) LinearLayout mNumVolLayout;
    @InjectView(R.id.startButton) Button mStartButton;
    @InjectView(R.id.okNumVol) Button mOkNumVol;
    @InjectView(R.id.editNumVol) EditText mNumvol;
    @InjectView(R.id.listButton) ImageButton mListButton;
    @InjectView(R.id.userButton) ImageButton mUserButton;
    @InjectView(R.id.weightLuggageButton) ImageButton mWeihtLuggageButton;
    @InjectView(R.id.sizeLuggageButton) ImageButton mSizeLuggageButton;
    @InjectView(R.id.logId) TextView mLogId;
    @InjectView(R.id.disconnectButton) Button mDisconnectButton;

    private Animation animUp, animDown;
    private String login;
    private Reservation mReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        mLogId.setText(R.string.welcom + login + "!");

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

    @OnClick (R.id.okNumVol)
    public void onClickOkNumVol(){
        String numRes = mNumvol.getText().toString().trim();
        if(numRes.isEmpty()){
            Toast.makeText(this, R.string.emptyNumVol, Toast.LENGTH_LONG).show();
        } else {
            String URL = Utils.BASE_URL + "api/reservations/" + numRes + "logins/" + login + ".json";

            StringRequest requestSendNumRes = new StringRequest(URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    try{
                        JSONObject jsonReservation = new JSONObject(s);

                        mReservation.setId(jsonReservation.getInt("id"));
                        mReservation.setNumRes(jsonReservation.getString("numRes"));
                        mReservation.setDate(jsonReservation.getString("date"));
                        mReservation.setTypeVoyageur(jsonReservation.getBoolean("typeVoyageur")? TypeVoyageur.TOURISTE : TypeVoyageur.AFFAIRE);
                        mReservation.setVol(new Vol());
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }Intent intent = new Intent(MainActivity.this, ProcessActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.listButton)
    public void onClickListButton(){
        Intent intent = new Intent(MainActivity.this, ForbiddenListActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.userButton)
    public void onClickUserButton(){
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
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
