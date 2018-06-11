package com.npdeas.b1k3labapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by NPDEAS on 22/03/2018.
 */


public class ConnectThread extends Thread {
    private BluetoothSocket socket = null;
    private BluetoothDevice device;

    private InputStream mmInStream = null;

    byte[] buffer = new byte[1024];

    public ConnectThread(BluetoothDevice device) {
        this.device = device;

        try {
            socket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
        } catch (IOException e) {
        }
        try {

            if (!socket.isConnected()) {
                socket.connect();
            }
            mmInStream = socket.getInputStream();

        } catch (IOException e) {
            Log.i("teste", "aqi deu erro  " + e.getMessage());
        }
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                socket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                mmInStream = socket.getInputStream();
                if (!socket.isConnected()) {
                    socket.connect();
                }
                mmInStream.read(buffer);
                Log.i("teste", "mds esta aqui " + buffer);
            } catch (Exception connectException) {
                Log.i("Bluetooth", "Erro :" + connectException.getMessage());
                /*try {
                    socket.close();
                } catch (IOException closeException) {
                }*/
            }
        }
    }

    public String getBluetoothResponse() {
        return new String(buffer);
    }

    public byte[] getBluetoothResponseB() {
        return buffer;
    }

    public void cancel() {
        try {
            this.interrupt();
            socket.close();

        } catch (IOException e) {
        }
    }
}