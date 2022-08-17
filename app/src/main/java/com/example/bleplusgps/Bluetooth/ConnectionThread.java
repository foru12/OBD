package com.example.bleplusgps.Bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bleplusgps.MainActivity;
import com.example.bleplusgps.R;

import java.io.IOException;
import java.lang.reflect.Method;

class ConnectionThread extends Thread   {
   private BluetoothSocket bluetoothSocket;
   private boolean succses = false;
   TextView txtStatus;
   MainActivity mainActivity;
   private Activity activity;

   public ConnectionThread(BluetoothDevice bluetoothDevice, Activity activity){
       this.activity = activity;
       try {
           Method method = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
           bluetoothSocket = (BluetoothSocket) method.invoke(bluetoothDevice,1);
       }
       catch (Exception e){
           e.printStackTrace();
       }
   }
   @SuppressLint("MissingPermission")
   @Override
   public void run(){
       txtStatus = activity.findViewById(R.id.txtStatus);
       try {
           bluetoothSocket.connect();
           succses = true;
       }
       catch (IOException e){
           e.printStackTrace();
           activity.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast.makeText(activity.getApplicationContext(),"FALSE",Toast.LENGTH_LONG).show();
                   txtStatus.setText(R.string.No_Success);
                   if (succses){
                      txtStatus.setText(R.string.Success);
                   }
               }
           });
           cancel();
       }
       if (succses){

           ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket);
           connectedThread.start();
       }
   }
   public void cancel(){
       try {
           bluetoothSocket.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

}