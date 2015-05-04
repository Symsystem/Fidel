package com.fidel.fidel.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.fidel.fidel.R;
import com.fidel.fidel.classes.ForbiddenObjects;
import com.fidel.fidel.adapters.ForbiddenObjectsAdapter;
import com.fidel.fidel.classes.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ForbiddenListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;
    private List<ForbiddenObjects> listForbid;
    private List<String> listString;

    @InjectView(R.id.listForbiddenObject) ListView mListForbidden;
    //@InjectView(android.R.id.empty)TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forbidden_list);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listString = Utils.readFile(this,R.raw.forbidden_object);
        listForbid = new ArrayList<ForbiddenObjects>();

        for(int i =0;i<listString.size();i++){
            listForbid.add(new ForbiddenObjects(listString.get(i)));
        }
        ForbiddenObjectsAdapter adapter = new ForbiddenObjectsAdapter(this, listForbid);
        mListForbidden.setAdapter(adapter);
       // mListForbidden.setEmptyView(empty);
        mListForbidden.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
