package com.example.bleplusgps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private ImageView imLocation,imOBD;
    private ImageButton btnBLList;
    private LinearLayout lnListLogGPS,lnListLogOBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initClick();
    }



    public void init(){
        btnBLList = findViewById(R.id.btnBLList);
        imLocation = findViewById(R.id.imLocation);
        imOBD = findViewById(R.id.imOBD);
        lnListLogGPS = findViewById(R.id.lnListLogGPS);
        lnListLogOBD = findViewById(R.id.lnListLogOBD);
    }
    public void initClick(){
        imLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imLocation.setVisibility(View.GONE);
                lnListLogGPS.setVisibility(View.VISIBLE);
            }
        });
        imOBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imOBD.setVisibility(View.GONE);
                lnListLogOBD.setVisibility(View.VISIBLE);
            }
        });
    }
}