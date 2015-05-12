package com.fidel.fidel.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fidel.fidel.R;
import com.fidel.fidel.adapters.AchatsAdapter;
import com.fidel.fidel.adapters.LuggagesAdapter;
import com.fidel.fidel.classes.Achat;
import com.fidel.fidel.classes.Bagage;
import com.fidel.fidel.classes.Reservation;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.request.OkHttpStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyLuggagesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;
    private Reservation mReservation;

    @InjectView(R.id.listBagages) ListView mListBagages;
    @InjectView(android.R.id.empty)TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_luggages);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                        List<Bagage> listBagage = new ArrayList<Bagage>();
                        for(int i = 0; i < arrayBagage.length(); i++){
                            JSONObject jsonBag = arrayBagage.getJSONObject(i);

                            Bagage bag = new Bagage(jsonBag.getInt("weight"),mReservation.getId());

                            listBagage.add(bag);
                        }
                        LuggagesAdapter adapter = new LuggagesAdapter(MyLuggagesActivity.this, listBagage);
                        mListBagages.setAdapter(adapter);
                        mListBagages.setEmptyView(empty);

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
                        Log.e("errorConnexion", volleyError.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(MyLuggagesActivity.this, new OkHttpStack());
        queue.add(requestBuy);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
