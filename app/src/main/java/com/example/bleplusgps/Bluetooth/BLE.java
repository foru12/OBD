package com.example.bleplusgps.Bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bleplusgps.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BLE extends AppCompatActivity implements AdapterView.OnItemClickListener{


    public static final int REQUEST_CODE_LOC = 1;
    private static final int BT_BOUNDED = 21;
    private static final int BT_SEARCH = 22;
    private static final int REQ_ENABLE = 0;
    private final int REQUEST_CODE_PERMISSION = 123;


    private ArrayList<BluetoothDevice> bluetoothDevices;
    private BluetoothAdapter bluetoothAdapter;
    private BtListAdapter listAdapter;
    private ListView ListBtDevices;
    private Button btnSearch,btnDisconnect;
    private ProgressBar progressBar;
    private FrameLayout frConnect;
    public TextView txtStatus;
    private ConnectedThread connectedThread;
    private ConnectionThread connectionThread;

    IntentFilter filter = new IntentFilter();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ble);



        initId();
        initClass();



        registerReceiver(broadcastReceiver,filter);

        //если bluetooth включен
        if (bluetoothAdapter.isEnabled()){
            //запускаем адаптер

            setListAdapter(BT_BOUNDED);


        }
        else{
            getStatusBt();
            setListAdapter(BT_BOUNDED);
        }


    }

    private void initClass() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<>();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);



    }

    private void initId() {
        ListBtDevices = findViewById(R.id.listBtDevices);
        progressBar = findViewById(R.id.progressBar);
        frConnect = findViewById(R.id.frConnect);
        txtStatus = findViewById(R.id.txtStatus);
        btnDisconnect = findViewById(R.id.btnDisconnect);
        btnSearch = findViewById(R.id.btnSearch);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if(connectedThread != null){
            connectedThread.cancel();
        }
        if(connectionThread != null){
            connectionThread.cancel();
        }
    }

    private void  setListAdapter(int type){
        bluetoothDevices.clear(); //очизаем адаптер

        switch (type){
            case BT_BOUNDED:
                bluetoothDevices = getbluetoothDeviceArrayList();
                listAdapter = new BtListAdapter(this,bluetoothDevices,R.drawable.bluetooth_1);

                break;
            case BT_SEARCH:
                listAdapter = new BtListAdapter(this,bluetoothDevices,R.drawable.bluetooth);

                break;
        }
        ListBtDevices.setAdapter(listAdapter);
        initClick();
    }
    //метод получения блютуз устройств
    private ArrayList<BluetoothDevice> getbluetoothDeviceArrayList(){
        @SuppressLint("MissingPermission") Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> tmpArraylist = new ArrayList<>();
        Log.e("ERRR",deviceSet.size() + "");
        Log.e("ERRR",tmpArraylist + "");
        if(deviceSet.size() > 0){
            for (BluetoothDevice blDevice: deviceSet) {
                tmpArraylist.add(blDevice);
            }
        }
        return tmpArraylist;
    }




    @SuppressLint("MissingPermission")
    private void getStatusBt(){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQ_ENABLE);
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnSearch(View view) {
        if (view.equals(btnSearch)) {
            enableSearch();
            Log.e("ERRRR","1---EEEEEE");
        }
        frConnect.setVisibility(View.GONE);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void enableSearch(){
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            Log.e("ERRRR","3---EEEEEE");
            accessLocationPermission();
        }
        else {
            Log.e("ERRRR","4---EEEEEE");
            bluetoothAdapter.startDiscovery();
        }
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.e("ERRRR","2--EEEEEE");
            switch (action){
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    btnSearch.setText(R.string.stop);
                    progressBar.setVisibility(View.VISIBLE);
                    Log.e("ERRRR","5--EEEEEE");
                    setListAdapter(BT_SEARCH);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.e("ERRRR","6--EEEEEE");
                    progressBar.setVisibility(View.GONE);
                    btnSearch.setText(R.string.start);
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    Log.e("ERRRR","7--EEEEEE");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device != null){
                        bluetoothDevices.add(device);
                        listAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            initClick();
        }
    };

    /**
     * Запрос на разрешение данных о местоположении (для Marshmallow 6.0)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void accessLocationPermission() {
        int accessCoarseLocation = this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocation   = this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listRequestPermission = new ArrayList<String>();

        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listRequestPermission.isEmpty()) {
            String[] strRequestPermission = listRequestPermission.toArray(new String[listRequestPermission.size()]);
            this.requestPermissions(strRequestPermission, REQUEST_CODE_LOC);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOC:

                if (grantResults.length > 0) {
                    for (int gr : grantResults) {
                        // Check if request is granted or not
                        if (gr != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    //TODO - Add your code here to start Discovery
                }
                break;
            default:
                return;
        }
    }


    public void btnDisconnect(View view) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        frConnect.setVisibility(View.VISIBLE);
        Log.e("onItemClick", "--------------");
        if (parent.equals(ListBtDevices)) {
            BluetoothDevice Device = bluetoothDevices.get(position);
            if (Device != null) {
                connectionThread = new ConnectionThread(Device, this);
                connectionThread.start();
            }
        }
    }
    public void initClick(){
        ListBtDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                frConnect.setVisibility(View.VISIBLE);
                Log.e("onItemClick", "--------------");
                if (parent.equals(ListBtDevices)) {
                    BluetoothDevice Device = bluetoothDevices.get(position);
                    if (Device != null) {
                        connectionThread = new ConnectionThread(Device, BLE.this);
                        connectionThread.start();
                    }
                }
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btnDisconnect","-------------------");
                if(connectedThread != null){
                    connectedThread.cancel();
                }
                if(connectionThread != null){
                    connectionThread.cancel();
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    public void fsdugb(View view) {
        Log.e("btnDisconnect","-------------------");

        if(connectedThread != null){
            connectedThread.cancel();
        }
        if(connectionThread != null){
            connectionThread.cancel();
        }
        bluetoothAdapter.disable();
    }
}