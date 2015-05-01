package com.fidel.fidel;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;

    @InjectView(R.id.numVolLayout) LinearLayout mNumVolLayout;
    @InjectView(R.id.startButton) Button mStartButton;

    boolean startClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNumVolLayout.setVisibility(View.GONE);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startClicked){
                    mStartButton.setText(R.string.cancel);
                    mNumVolLayout.setVisibility(View.VISIBLE);
                }
                else{
                    mStartButton.setText(R.string.start);
                    mNumVolLayout.setVisibility(View.GONE);
                }
                startClicked = !startClicked;
            }
        });

    }
}
