package com.example.bleplusgps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Splash extends AppCompatActivity {


    private final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        CALL_PERMISSION();
        check();
    }

    @Override
    protected void onResume () {
        super.onResume();CALL_PERMISSION();
        check();
    }

    //Проверяем включено ли местоположение
    public void check () {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }
    }

    private void buildAlertMessageNoGps () {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.startGPS).setCancelable(false).setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void CALL_PERMISSION() {

        int permissionStatusCouarse = ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionStatusFine = ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionStatusCouarse == PackageManager.PERMISSION_GRANTED || permissionStatusFine == PackageManager.PERMISSION_GRANTED) {


        } else {
            ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        }

    }




}
