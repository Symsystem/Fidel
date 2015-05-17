package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    @InjectView(R.id.shoppingButton) ImageButton mShoppingButton;
    @InjectView(R.id.myLuggagesButton) ImageButton mMyLuggagesButton;
    @InjectView(R.id.giveUpButton) Button mGiveUpButton;
    @InjectView(R.id.finishButton) Button mFinishButton;
    @InjectView(R.id.closeTextView) IconTextView mCloseTextView;
    @InjectView(R.id.closeTextView2) IconTextView mCloseTextView2;
    @InjectView(R.id.addOkButton) Button mAddLuggageOkButton;
    @InjectView(R.id.weightLuggageEdit) EditText mWeightLuggageEdit;
    @InjectView(R.id.addLuggageButton) ImageButton mAddLuggagesButton;
    @InjectView(R.id.forbiddenList) ImageButton mForbiddenList;
    @InjectView(R.id.sizeLuggageButton2) ImageButton mSizeLuggage;
    @InjectView(R.id.weightLuggageButton2) ImageButton mWeightLuggage;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.spinnerPersonnes) Spinner mPersonneSpinner;

    private Animation scaleDownAnim, scaleUpAnim;
    private int selectedPersonne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(mProgressBar.getVisibility() == ProgressBar.VISIBLE) {
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
        Intent intent = getIntent();

        mReservation = (Reservation)intent.getSerializableExtra("reservation");
        mNumRes.setText(getResources().getString(R.string.number) + mReservation.getNumRes());

        selectedPersonne = 0;
        String personnes[] = new String[mReservation.getListPersonne().size()];
        for(int i = 0; i<mReservation.getListPersonne().size(); i++){
            personnes[i] = mReservation.getListPersonne().get(i).getPrenom()
                    + " " + mReservation.getListPersonne().get(i).getNom();
        }
        mPersonneSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, personnes));
        mPersonneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPersonne = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        if(mReservation.getNumLuggages() > 0){
            mGiveUpButton.setVisibility(View.GONE);
        }

        scaleDownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUpAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        mCloseTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                if(action == MotionEvent.ACTION_DOWN){
                    mInfoButton.setVisibility(View.VISIBLE);
                    mInfoButton.startAnimation(scaleUpAnim);
                }

                return true;
            }
        });

        mCloseTextView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                if(action == MotionEvent.ACTION_DOWN){
                    mAddLuggagesButton.setVisibility(View.VISIBLE);
                    mAddLuggagesButton.startAnimation(scaleUpAnim);
                }

                return true;
            }
        });
    }

    @OnClick (R.id.infoButton)
    public void onClickInfoButton(){
        mInfoButton.startAnimation(scaleDownAnim);
        mInfoButton.setVisibility(View.INVISIBLE);
    }

    @OnClick (R.id.ticketButton)
    public void onClickTicketButton(){
        Intent intent = new Intent(ProcessActivity.this, TicketActivity.class);
        intent.putExtra("reservation", mReservation);
        startActivity(intent);
    }

   @OnClick (R.id.shoppingButton)
    public void onClickShoppingButton(){
        Intent intent = new Intent(ProcessActivity.this, ShoppingActivity.class);
        intent.putExtra("reservation", mReservation);
        startActivity(intent);
    }

    @OnClick (R.id.myLuggagesButton)
    public void onClickMyLuggagesButton(){
        Intent intent = new Intent(ProcessActivity.this, MyLuggagesActivity.class);
        intent.putExtra("reservation", mReservation);
        startActivity(intent);
    }

    @OnClick (R.id.addLuggageButton)
    public void onClickAddLuggagesButton(){
        mAddLuggagesButton.startAnimation(scaleDownAnim);
        mAddLuggagesButton.setVisibility(View.INVISIBLE);
    }

    @OnClick (R.id.addOkButton)
    public void onClickAddOkButton(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("weight", mWeightLuggageEdit.getText().toString().trim());
        params.put("idRes", String.valueOf(mReservation.getId()));
        params.put("persId", String.valueOf(mReservation.getListPersonne().get(selectedPersonne).getId()));
        String URL = Utils.BASE_URL + "api/bagages.json";

        PostRequest requestAddLug = new PostRequest(URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    JSONObject j = new JSONObject(s);
                    if(j.has("response")) {
                        if (j.getInt("response") == Utils.SUCCESS) {
                            Toast.makeText(ProcessActivity.this, "Bagage ajouté !", Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(ProgressBar.GONE);
                            if (mGiveUpButton.getVisibility() == View.VISIBLE) {
                                mGiveUpButton.setVisibility(View.GONE);
                            }
                        }
                        else if(j.getInt("response") == Utils.BAGAGE_KO){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProcessActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Vous avez trop de bagages ou le poids est trop élevé");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProcessActivity.this);
                        builder.setTitle("Erreur");
                        builder.setMessage("Pas d'accès au serveur, veuillez patienter");
                        builder.setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        mProgressBar.setVisibility(ProgressBar.GONE);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(ProcessActivity.this, new OkHttpStack());
        queue.add(requestAddLug);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
    }


    @OnClick (R.id.giveUpButton)
    public void onClickGiveUpButton(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("resId", ""+mReservation.getId());
        String URL = Utils.BASE_URL + "api/giveups.json";

        PostRequest requestGiveUp = new PostRequest(URL, params, new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response") && userJSON.getInt("response")==Utils.SUCCESS){
                        Intent intent = new Intent(ProcessActivity.this, MainActivity.class);
                        intent.putExtra("user", mReservation.getUser());
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProcessActivity.this);
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
                        Log.e("errorConnexion", volleyError.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(ProcessActivity.this, new OkHttpStack());
        queue.add(requestGiveUp);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @OnClick (R.id.finishButton)
    public void onClickFinishButton(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("numRes", mReservation.getNumRes());
        params.put("userId", "" + mReservation.getUser().getId());
        String URL = Utils.BASE_URL + "api/finishes.json";

        PostRequest requestGiveUp = new PostRequest(URL, params, new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response") && userJSON.getInt("response")==Utils.SUCCESS){
                        Intent intent = new Intent(ProcessActivity.this, MainActivity.class);
                        intent.putExtra("user", mReservation.getUser());
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProcessActivity.this);
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
                        Log.e("errorConnexion", volleyError.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(ProcessActivity.this, new OkHttpStack());
        queue.add(requestGiveUp);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @OnClick (R.id.forbiddenList)
    public void onClickForbiddenList(){
        Intent intent = new Intent(ProcessActivity.this,ForbiddenListActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.sizeLuggageButton2)
    public void onClickSizeLuggageButton(){
        Intent intent = new Intent(ProcessActivity.this, SizeLuggageActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.weightLuggageButton2)
    public void onClickWeightLuggageButton(){
        Intent intent = new Intent(ProcessActivity.this, WeightLuggageActivity.class);
        startActivity(intent);
    }


}
