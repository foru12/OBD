package com.example.bleplusgps.GPS;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.bleplusgps.R;

import org.w3c.dom.Text;

import java.util.Date;

public class GPS {


    TextView tvLocationNetLatitude;
    TextView tvStatusGPS;
    TextView tvStatusNET;
    TextView tvLocationNetLongitude;
    TextView tvLocationNetTime;

    LocationManager locationManager;
    ComponentActivity componentActivity;
    boolean permissionWorkflowInProgress;


    public GPS(ComponentActivity componentActivity, Activity activity) {
        this.componentActivity = componentActivity;
        this.activity = activity;
    }

    Activity activity;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initIdGPS(Activity activity){
        //status
        tvStatusGPS = activity.findViewById(R.id.tvTitleGPS);
        tvStatusNET = activity.findViewById(R.id.tvStatusNET);


        //GPS input
        tvLocationNetLatitude = activity.findViewById(R.id.tvLocationNetLatitude);
        tvLocationNetLongitude = activity.findViewById(R.id.tvLocationNetLongitude);
        tvLocationNetTime = activity.findViewById(R.id.tvLocationNetTime);


        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

    }

    private final ActivityResultLauncher<Intent> batteryOptimizationLauncher = componentActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            //LOG.debug(String.valueOf(result.getResultCode()));
                            String packageName =activity.getPackageName();
                            PowerManager pm = (PowerManager)activity.getSystemService(Context.POWER_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!pm.isIgnoringBatteryOptimizations(packageName)){
                                    //                    if(result.getResultCode() != Activity.RESULT_OK){
                                    //LOG.warn("Request to ignore battery optimization was denied.");
                                }
                                else {
                                    //LOG.debug("Request to ignore battery optimization was granted.");
                                }
                            }
                            permissionWorkflowInProgress=false;
                        }
                    });


         LocationListener locationListener = new LocationListener() {
        // новые данные о местоположении
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }
        //указанный провайдер был отключен юзером. В этом методе вызываем свой метод checkEnabled, который на экране обновит текущие статусы провайдеров.
        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatusGPS.setText("Status: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNET.setText("Status: " + String.valueOf(status));
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {

            tvLocationNetLatitude.setText("Долгота: " + location.getLatitude());
            tvLocationNetLongitude.setText("Широта: " + location.getLongitude());
            tvLocationNetTime.setText("Время: " + new Date(
                    location.getTime()).toString());
            Log.e("ШИРОТА", "" + location.getLatitude());
            Log.e("ДОЛГОТА", "" + location.getLongitude());
            Log.e("ВРЕМЯ", "" + new Date(
                    location.getTime()));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            tvLocationNetLatitude.setText("Долгота: " + location.getLatitude());
            tvLocationNetLongitude.setText("Широта: " + location.getLongitude());
            tvLocationNetTime.setText("Время: " + new Date(
                    location.getTime()).toString());
            Log.e("ШИРОТА", "" + location.getLatitude());
            Log.e("ДОЛГОТА", "" + location.getLongitude());
            Log.e("ВРЕМЯ", "" + new Date(
                    location.getTime()));
        }
    }


    @SuppressLint("SetTextI18n")
    private void checkEnabled() {
        tvStatusGPS.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        tvStatusNET.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }



    /*  TextView tvEnabledGPS;
    TextView tvStatusGPS;
    TextView tvLocationGPS;
    TextView tvEnabledNet;
    TextView tvStatusNet;
    TextView tvLocationNet;
    TextView tvLocationNetLatitude;
    TextView tvLocationNetLongitude;
    TextView tvLocationNetTime;

    Button btnShow;

    LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();
    private boolean permissionWorkflowInProgress;
    //private static final Logger LOG = Logs.of(GpsMainActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


    }


    private final ActivityResultLauncher<Intent> batteryOptimizationLauncher =
             registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            //LOG.debug(String.valueOf(result.getResultCode()));
                            String packageName = getPackageName();
                            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!pm.isIgnoringBatteryOptimizations(packageName)){
                                    //                    if(result.getResultCode() != Activity.RESULT_OK){
                                    //LOG.warn("Request to ignore battery optimization was denied.");
                                }
                                else {
                                    //LOG.debug("Request to ignore battery optimization was granted.");
                                }
                            }
                            permissionWorkflowInProgress=false;
                        }
                    });

    public void initView() {
        tvEnabledGPS = findViewById(R.id.tvEnabledGPS);
        tvStatusGPS = findViewById(R.id.tvStatusGPS);
        tvLocationGPS = findViewById(R.id.tvLocationGPS);
        tvEnabledNet = findViewById(R.id.tvEnabledNet);
        tvStatusNet = findViewById(R.id.tvStatusNet);
        tvLocationNet = findViewById(R.id.tvLocationNet);
        tvLocationNetLatitude = findViewById(R.id.tvLocationNetLatitude);//Широта
        tvLocationNetLongitude = findViewById(R.id.tvLocationNetLongitude);//Долгота
        tvLocationNetTime = findViewById(R.id.tvLocationNetTime);//Время
        btnShow = findViewById(R.id.btnShow);//Показать данные
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);
        checkEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }



    @SuppressLint("SetTextI18n")
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {

            tvLocationNetLatitude.setText("Долгота: " + location.getLatitude());
            tvLocationNetLongitude.setText("Широта: " + location.getLongitude());
            tvLocationNetTime.setText("Время: " + new Date(
                    location.getTime()).toString());
            Log.e("ШИРОТА", "" + location.getLatitude());
            Log.e("ДОЛГОТА", "" + location.getLongitude());
            Log.e("ВРЕМЯ", "" + new Date(
                    location.getTime()));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            tvLocationNetLatitude.setText("Долгота: " + location.getLatitude());
            tvLocationNetLongitude.setText("Широта: " + location.getLongitude());
            tvLocationNetTime.setText("Время: " + new Date(
                    location.getTime()).toString());
            Log.e("ШИРОТА", "" + location.getLatitude());
            Log.e("ДОЛГОТА", "" + location.getLongitude());
            Log.e("ВРЕМЯ", "" + new Date(
                    location.getTime()));
        }
    }


    @SuppressLint("SetTextI18n")
    private void checkEnabled() {
        tvEnabledGPS.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        tvEnabledNet.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(MainActivityStart.this, com.example.gps.OBD.MainActivity.class));


    };
*/





}
