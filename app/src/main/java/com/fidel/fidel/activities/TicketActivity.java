package com.fidel.fidel.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.adapters.PersonnesAdapter;
import com.fidel.fidel.classes.Personne;
import com.fidel.fidel.classes.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TicketActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    //Cette activité permet à l'utilisateur de venir voir les différentes données relatives à son vol, comme l'heure d'embarquement, le numéro de porte, les numéros de sièges, etc.

    Toolbar toolbar;
    private Reservation mReservation;

    @InjectView(R.id.numResContent) TextView mNumResContent;
    @InjectView(R.id.dateContent) TextView mDateContent;
    @InjectView(R.id.typeVoyageurContent) TextView mTypeVoyageurContent;
    @InjectView(R.id.typeVolContent) TextView mTypeVolContent;
    @InjectView(R.id.idPorte) TextView mGate;
    @InjectView(R.id.heureDepart) TextView mHDepart;
    @InjectView(R.id.heureEmbarquement) TextView mHEmbarquement;
    @InjectView(R.id.heureArrivee) TextView mHArrivee;

    @InjectView(R.id.listPersonnes) ExpandableListView mListPersonne;
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

        Date dateRes = null, hDepart = null, hEmbarquement = null, hArrivee = null;
        try {
            dateRes = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'SSSS").parse(mReservation.getDate());
            hDepart = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'SSSS").parse(mReservation.getVol().getHeureDepart());
            hEmbarquement = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'SSSS").parse(mReservation.getVol().getHeureEmbarquement());
            hArrivee = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'SSSS").parse(mReservation.getVol().getHeureArrivee());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDateRes = new SimpleDateFormat("dd / MM / yyyy").format(dateRes);
        String formattedHDepart = new SimpleDateFormat("HH:mm").format(hDepart);
        String formattedHEmbarquement = new SimpleDateFormat("HH:mm").format(hEmbarquement);
        String formattedHArrivee = new SimpleDateFormat("HH:mm").format(hArrivee);


        mNumResContent.setText(String.valueOf(mReservation.getNumRes()));
        mDateContent.setText(formattedDateRes);

        mTypeVoyageurContent.setText(String.valueOf(mReservation.getTypeVoyageur()));
        mTypeVolContent.setText(String.valueOf(mReservation.getVol().getTypeVol()));
        mGate.setText(String.valueOf(mReservation.getVol().getGate()));
        mHDepart.setText(formattedHDepart);
        mHEmbarquement.setText(formattedHEmbarquement);
        mHArrivee.setText(formattedHArrivee);

        ArrayList<Personne> listPersonnes = mReservation.getListPersonne();
        ArrayList<String> listNomsPersonnes = new ArrayList<>();

        for(int i = 0; i<listPersonnes.size(); i++){
            listNomsPersonnes.add(listPersonnes.get(i).getPrenom() + " " + listPersonnes.get(i).getNom());
        }

        mListPersonne.setDividerHeight(2);
        mListPersonne.setGroupIndicator(null);
        mListPersonne.setClickable(true);

        PersonnesAdapter adapter = new PersonnesAdapter(this,
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                listNomsPersonnes, listPersonnes, mReservation);
        mListPersonne.setAdapter(adapter);
        mListPersonne.setEmptyView(empty);
        mListPersonne.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
