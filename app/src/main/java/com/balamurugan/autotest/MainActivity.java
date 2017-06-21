package com.balamurugan.autotest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.balamurugan.autotest.blueconn.DeviceList;

public class MainActivity extends AppCompatActivity {

    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//
        // title = "Autonimbus";
        // Bundle bundle = this.getIntent().getExtras();
        //resourceID = bundle.getInt("rid");
        //title = bundle.getString("title");

        setContentView(R.layout.activity_main);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

*/

        setTitle("Autonimbus");


        Button blueButton = (Button) findViewById(R.id.email_sign_in_button);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new GetRoute().execute();
                Intent intent = new Intent(MainActivity.this, DeviceList.class);
                //intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }

}


