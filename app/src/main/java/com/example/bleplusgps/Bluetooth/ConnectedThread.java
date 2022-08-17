package com.example.bleplusgps.Bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConnectedThread extends Thread{
    private final InputStream inputStream;
    private final OutputStream outputStream;


    public ConnectedThread(BluetoothSocket bluetoothSocket){

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {

    }

    public void wrire(String command){
        byte[] bytes = command.getBytes();
        if(outputStream != null){
            try {
                outputStream.write(bytes);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void cancel(){
        try{
            inputStream.close();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
