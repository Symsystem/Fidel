package com.fidel.fidel;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ConnectActivity extends ActionBarActivity {

    @InjectView(R.id.inscriptionButton) Button mInscrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ButterKnife.inject(this);
    }

    @OnClick (R.id.inscriptionButton)
    public void onClickInscrButton(){
        Intent intent = new Intent(ConnectActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
