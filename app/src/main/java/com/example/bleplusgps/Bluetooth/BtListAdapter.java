package com.example.bleplusgps.Bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bleplusgps.R;

import java.util.ArrayList;
import java.util.Objects;

public class BtListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private int iconType;
    private static final int RESOURCE = R.layout.ble_list_item;

    public BtListAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDevices, int iconType) {
        this.bluetoothDevices = bluetoothDevices;
        this.iconType = iconType;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bluetoothDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("MissingPermission")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtDevice,txtMac;
        ImageView imStatus;
        RelativeLayout Item;

        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(RESOURCE,parent,false);

        txtDevice = view.findViewById(R.id.txtDevice);
        txtMac = view.findViewById(R.id.txtMac);
        imStatus = view.findViewById(R.id.imStatus);


        BluetoothDevice device = bluetoothDevices.get(position);
        if (device != null){
            if (Objects.equals(device.getName(), "")) txtDevice.setText("No Name");
           txtDevice.setText(device.getName());
           txtMac.setText(device.getAddress());
           imStatus.setImageResource(iconType);

        }


        return view;
    }
}
