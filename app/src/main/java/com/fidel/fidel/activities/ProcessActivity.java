package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
import com.fidel.fidel.classes.Reservation;
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

public class ProcessActivity extends ActionBarActivity {

    Toolbar toolbar;
    private Reservation mReservation;

    @InjectView(R.id.numResId) TextView mNumRes;
    @InjectView(R.id.infoButton) ImageButton mInfoButton;
    @InjectView(R.id.ticketButton) ImageButton mTicketButton;
    @InjectView(R.id.freeServicesButton) ImageButton mFreeServicesButton;
    @InjectView(R.id.myLuggagesButton) ImageButton mMyLuggagesButton;
    @InjectView(R.id.addLuggageButton) ImageButton mAddLuggagesButton;
    @InjectView(R.id.giveUpButton) Button mGiveUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        mReservation = (Reservation)intent.getSerializableExtra("reservation");
        mNumRes.setText(R.string.number + mReservation.getNumRes());
    }

    @OnClick (R.id.infoButton)
    public void onClickInfoButton(){
        //J'ATTENDS SYMEON
    }

    @OnClick (R.id.ticketButton)
    public void onClickTicketButton(){
        Intent intent = new Intent(ProcessActivity.this, TicketActivity.class);
        intent.putExtra("reservation", mReservation);
        startActivity(intent);
    }

    @OnClick (R.id.freeServicesButton)
    public void onClickFreeServicesButton(){
        Intent intent = new Intent(ProcessActivity.this, FreeServicesActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.myLuggagesButton)
    public void onClickMyLuggagesButton(){
        Intent intent = new Intent(ProcessActivity.this, MyLuggagesActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.addLuggageButton)
    public void onClickAddLuggagesButton(){
        Intent intent = new Intent(ProcessActivity.this, AddLuggageActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.giveUpButton)
    public void onClickGiveUpButton(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("numRes", mReservation.getNumRes());
        String URL = Utils.BASE_URL + "api/giveUp/" + mReservation.getNumRes() + ".json";

        PostRequest requestGiveUp = new PostRequest(URL, params, new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response") && userJSON.getInt("response")==Utils.SUCCESS){
                        Intent intent = new Intent(ProcessActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProcessActivity.this);
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
        RequestQueue queue = Volley.newRequestQueue(ProcessActivity.this, new OkHttpStack());
        queue.add(requestGiveUp);
    }


}
