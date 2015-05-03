package com.fidel.fidel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.adapters.PersonnesAdapter;
import com.fidel.fidel.classes.Reservation;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TicketActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    Toolbar toolbar;
    private Reservation mReservation;

    @InjectView(R.id.numResId) TextView mNumRes;
    @InjectView(R.id.dateId) TextView mDate;
    @InjectView(R.id.typeVoyageurId) TextView mTypeVoyageur;
    @InjectView(R.id.typeVolId) TextView mTypeVol;

    @InjectView(R.id.listPersonnes) ListView mListPersonne;
    @InjectView(android.R.id.empty)TextView empty;

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

        mTypeVoyageur.setText("Billet(s) de classe " + mReservation.getTypeVoyageur());
        mTypeVol.setText(("Vol de type " + mReservation.getVol().getTypeVol()));

        PersonnesAdapter adapter = new PersonnesAdapter(this, mReservation.getListPersonne());
        mListPersonne.setAdapter(adapter);
        mListPersonne.setEmptyView(empty);
        mListPersonne.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
