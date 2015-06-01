package com.fidel.fidel.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fidel.fidel.R;

import butterknife.ButterKnife;

public class SizeLuggageActivity extends ActionBarActivity {

    //Cette activité permet à l'utilisateur de se tenir au courant des normes quant aux tailles réglementaires des bagages

    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_luggage);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


}
