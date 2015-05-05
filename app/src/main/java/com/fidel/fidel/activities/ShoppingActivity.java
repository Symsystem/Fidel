package com.fidel.fidel.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.fidel.fidel.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShoppingActivity extends ActionBarActivity {

    Toolbar toolbar;

    @InjectView(R.id.totalCostId) TextView mTotalCost;
    @InjectView(R.id.annuleButton) Button mAnnulerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @OnClick (R.id.annuleButton)
    public void onClickAnnuleButton(){
        Intent intent = new Intent(ShoppingActivity.this, ProcessActivity.class);
        startActivity(intent);
    }

}
