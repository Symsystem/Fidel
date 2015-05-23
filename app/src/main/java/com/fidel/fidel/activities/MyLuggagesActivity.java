package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
import com.fidel.fidel.adapters.LuggagesAdapter;
import com.fidel.fidel.classes.Bagage;
import com.fidel.fidel.classes.Reservation;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.request.OkHttpStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyLuggagesActivity extends ActionBarActivity {

    Toolbar toolbar;
    private Reservation mReservation;

    @InjectView(R.id.listBagages) ListView mListBagages;
    @InjectView(R.id.spinner) ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_luggages);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSpinner.setVisibility(View.VISIBLE);
        mListBagages.setVisibility(View.GONE);

        Intent intent = getIntent();
        mReservation = (Reservation) intent.getSerializableExtra("reservation");

        String URL = Utils.BASE_URL + "api/bagages/" + mReservation.getId() + ".json";

        StringRequest requestBuy = new StringRequest(URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response") && userJSON.getInt("response")==Utils.SUCCESS) {

                        JSONArray arrayBagage = userJSON.getJSONArray("bagage");
                        ArrayList<Bagage> listBagage = new ArrayList<>();

                        double totalWeight = 0;
                        for(int i = 0; i < arrayBagage.length(); i++){
                                JSONObject jsonBag = arrayBagage.getJSONObject(i);
                            double w = jsonBag.getDouble("weight");
                            Bagage bag = new Bagage(jsonBag.getInt("id"),w ,mReservation.getId());
                            listBagage.add(bag);

                            totalWeight += w;
                        }

                        View totLuggages =  ((LayoutInflater)MyLuggagesActivity.this
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.total_luggages, null, false);

                        TextView totalWeightView = (TextView) totLuggages.findViewById(R.id.totWeight);
                        TextView totalNbrView = (TextView) totLuggages.findViewById(R.id.totNbrLuggage);
                        totalWeightView.setText("Poids : " + totalWeight + " Kg");
                        totalNbrView.setText(listBagage.size() + " bagages");

                        mSpinner.setVisibility(View.GONE);
                        LuggagesAdapter adapter = new LuggagesAdapter(MyLuggagesActivity.this, listBagage);

                        mListBagages.addFooterView(totLuggages);
                        mListBagages.setAdapter(adapter);
                        mListBagages.setVisibility(View.VISIBLE);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyLuggagesActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyLuggagesActivity.this);
                        builder.setTitle("Erreur");
                        builder.setMessage("Pas d'accès Internet, veuillez l'activer");
                        builder.setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Log.e("errorConnexion", volleyError.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(MyLuggagesActivity.this, new OkHttpStack());
        queue.add(requestBuy);
    }

}
