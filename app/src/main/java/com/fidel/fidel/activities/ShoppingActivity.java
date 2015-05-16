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
import com.fidel.fidel.classes.Reservation;
import com.fidel.fidel.classes.User;
import com.fidel.fidel.classes.Utils;
import com.fidel.fidel.request.OkHttpStack;
import com.fidel.fidel.request.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShoppingActivity extends ActionBarActivity{

    Toolbar toolbar;
    private Reservation mReservation;
    private double totalCost = 0.0;
    private String codeAchat;

    @InjectView(R.id.totalCostId) TextView mTotalCost;
    @InjectView(R.id.annuleButton) Button mAnnulerButton;
    @InjectView(R.id.layoutLoadId) RelativeLayout mLayoutLoad;
    @InjectView(R.id.layoutShowId) RelativeLayout mLayoutShow;
    @InjectView(R.id.listAchats) ListView mListAchats;
    @InjectView(R.id.layoutEnterNbr) RelativeLayout mLayoutNbr;
    @InjectView(android.R.id.empty)TextView empty;
    @InjectView(R.id.validationButton) Button mValidationButton;
    @InjectView(R.id.editEnterNbr) EditText mCodeAchat;
    @InjectView(R.id.shopNameId) TextView mShopName;
    @InjectView(R.id.buyButton) Button mBuyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        mReservation = (Reservation)intent.getSerializableExtra("reservation");

    }

    @OnClick (R.id.annuleButton)
    public void onClickAnnuleButton(){
        Intent intent = new Intent(ShoppingActivity.this, ProcessActivity.class);
        intent.putExtra("reservation",mReservation);
        startActivity(intent);
    }

    @OnClick (R.id.validationButton)
    public void onClickValidationButton(){


        codeAchat = mCodeAchat.getText().toString().trim();

        String URL = Utils.BASE_URL + "api/achats/" + codeAchat + ".json";

        StringRequest requestBuy = new StringRequest(URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject buyJSON = new JSONObject(s);
                    if (buyJSON.has("response") && buyJSON.getInt("response")==Utils.SUCCESS) {
                        mLayoutLoad.setVisibility(RelativeLayout.GONE);
                        mLayoutShow.setVisibility(RelativeLayout.VISIBLE);
                        mBuyButton.setVisibility(View.VISIBLE);;
                        mShopName.setText(buyJSON.getString("shopName"));
                        JSONArray arrayAchats = buyJSON.getJSONArray("details");
                        List<Achat> listAchat = new ArrayList<Achat>();

                        for(int i = 0; i < arrayAchats.length(); i++){
                            JSONObject jsonBuy = arrayAchats.getJSONObject(i);

                            Achat buy = new Achat(
                            jsonBuy.getString("name"),
                            jsonBuy.getInt("quantity"),
                            jsonBuy.getDouble("price"));

                            totalCost += (buy.getPrix()*buy.getQuantite());
                            mTotalCost.setText(totalCost + "€");

                            listAchat.add(buy);
                        }
                        AchatsAdapter adapter = new AchatsAdapter(ShoppingActivity.this, listAchat);
                        mListAchats.setAdapter(adapter);
                        mListAchats.setEmptyView(empty);

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
        mLayoutNbr.setVisibility(RelativeLayout.GONE);
        mLayoutLoad.setVisibility(RelativeLayout.VISIBLE);
    }

    @OnClick (R.id.buyButton)
    public void onClickBuyButton(){
        if(mReservation.getUser().getWallet() < totalCost){
            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
            builder.setTitle("Attention");
            builder.setMessage("Vous n'avez pas assez de crédit");
            builder.setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", String.valueOf(mReservation.getUser().getId()));
            params.put("codeAchat", codeAchat);
            String URL = Utils.BASE_URL + "api/achats/users.json";

            PostRequest requestBuyShopping = new PostRequest(URL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try{
                        JSONObject objectJSON = new JSONObject(s);
                        if(objectJSON.getInt("response") == Utils.SUCCESS){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
                            builder.setTitle("Achat effectué");
                            builder.setMessage("Le total a été débité de votre crédit");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mReservation.getUser().setWallet(mReservation.getUser().getWallet() - totalCost);
                            Intent intent = new Intent(ShoppingActivity.this,ProcessActivity.class);
                            intent.putExtra("reservation", mReservation);
                            startActivity(intent);
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
            RequestQueue queue = Volley.newRequestQueue(ShoppingActivity.this, new OkHttpStack());
            queue.add(requestBuyShopping);
        }
    }



}
