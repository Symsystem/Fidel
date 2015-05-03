package com.fidel.fidel.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.classes.Reservation;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TicketActivity extends ActionBarActivity {

    Toolbar toolbar;
    private Reservation mReservation;

    @InjectView(R.id.numResId) TextView mNumRes;
    @InjectView(R.id.dateId) TextView mDate;
    @InjectView(R.id.typeVoyageurId) TextView mTypeVoyageur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        mReservation = (Reservation)intent.getSerializableExtra("reservation");

        mNumRes.setText("Réservation N° : " + mReservation.getNumRes());
        mDate.setText("Date de réservation : " + mReservation.getDate());
        mTypeVoyageur.setText("Billet de classe " + mReservation.getTypeVoyageur());



    }

}
