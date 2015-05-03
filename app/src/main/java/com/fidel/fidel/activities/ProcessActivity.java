package com.fidel.fidel.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fidel.fidel.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProcessActivity extends ActionBarActivity {

    Toolbar toolbar;

    @InjectView(R.id.numResId) TextView mNumRes;
    @InjectView(R.id.infoButton) ImageButton mInfoButton;
    @InjectView(R.id.ticketButton) ImageButton mTicketButton;
    @InjectView(R.id.freeServicesButton) ImageButton mFreeServicesButton;
    @InjectView(R.id.myLuggagesButton) ImageButton mMyLuggagesButton;
    @InjectView(R.id.addLuggageButton) ImageButton mAddLuggagesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String numRes = intent.getStringExtra("numRes");
        mNumRes.setText(R.string.number + numRes);
    }


}
