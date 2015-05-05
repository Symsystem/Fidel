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
import android.widget.Button;
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
import com.fidel.fidel.classes.Achat;
import com.fidel.fidel.classes.User;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.request.OkHttpStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShoppingActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    Toolbar toolbar;

    @InjectView(R.id.totalCostId) TextView mTotalCost;
    @InjectView(R.id.annuleButton) Button mAnnulerButton;
    @InjectView(R.id.layoutLoadId) RelativeLayout mLayoutLoad;
    @InjectView(R.id.layoutShowId) RelativeLayout mLayoutShow;
    @InjectView(R.id.listAchats) ListView mListAchats;
    @InjectView(android.R.id.empty)TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        int userId = (int)((User)intent.getSerializableExtra("user")).getId();

        String URL = Utils.BASE_URL + "api/achat/" + userId + ".json";

        StringRequest requestBuy = new StringRequest(URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response") && userJSON.getInt("response")==Utils.SUCCESS) {
                        mLayoutLoad.setVisibility(RelativeLayout.GONE);
                        mLayoutShow.setVisibility(RelativeLayout.VISIBLE);

                        JSONArray arrayAchats = userJSON.getJSONArray("achats");
                        List<Achat> listAchat = new List<Achat>
                        for(int i = 0; i < arrayAchats.length(); i++){
                            JSONObject jsonBuy = arrayAchats.getJSONObject(i);

                            Achat buy = new Achat();
                            buy.setLibelle(jsonBuy.getString("libelle"));
                            buy.setPrix(jsonBuy.getDouble("prix"));
                            buy.setQuantite(jsonBuy.getInt("quantite"));

                            mReservation.getListPersonne().add(pers);
                        }

                        AchatsAdapter adapter = new AchatsAdapter(this, ATTENTION A COMPLETER);
                        mListAchats.setAdapter(adapter);
                        mListAchats.setEmptyView(empty);
                        mListAchats.setOnItemClickListener(this);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
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
        RequestQueue queue = Volley.newRequestQueue(ShoppingActivity.this, new OkHttpStack());
        queue.add(requestBuy);
    }

    @OnClick (R.id.annuleButton)
    public void onClickAnnuleButton(){
        Intent intent = new Intent(ShoppingActivity.this, ProcessActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
