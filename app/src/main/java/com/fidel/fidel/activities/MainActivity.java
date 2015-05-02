package com.fidel.fidel.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fidel.fidel.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;

    @InjectView(R.id.numVolLayout) LinearLayout mNumVolLayout;
    @InjectView(R.id.startButton) Button mStartButton;

    private Animation animUp, animDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNumVolLayout.setVisibility(View.INVISIBLE);

        animUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        animDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);


        // Made visible the flight number textfield and valid button
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNumVolLayout.getVisibility() == View.INVISIBLE){
                    mStartButton.setText(R.string.cancel);
                    mNumVolLayout.setVisibility(View.VISIBLE);
                    mNumVolLayout.startAnimation(animDown);
                }
                else{
                    mStartButton.setText(R.string.start);
                    mNumVolLayout.startAnimation(animUp);
                    mNumVolLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
