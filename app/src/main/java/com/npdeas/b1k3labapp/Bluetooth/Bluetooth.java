package com.npdeas.b1k3labapp.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import com.npdeas.b1k3labapp.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

/**
 * Created by NPDEAS on 09/04/2018.
 */

public class Bluetooth extends Thread{

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int size = 0;
    private int lastDisRead = 0;
    private byte[] buffer = new byte[1024];

    public Bluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // se nao tem bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {//caso o bluetooth nao esteja ativado, o ativa
            mBluetoothAdapter.enable();
            /*Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, Constants.REQUEST_OK);*/
        }
    }


    public Bluetooth(Activity activity,String mac){
        this();
        File macFile = new File(activity.getFilesDir() + "/B1K3_Lab", Constants.MAC_FILE);
        if (macFile.exists()) {
            try {
                Scanner scanner = new Scanner(macFile);
                mac = scanner.nextLine();
                this.getDeviceByMAC(mac);
            } catch (Exception e) {
                Log.i("Bluetooth",e.getMessage());
            }
        }


    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(200);
                size = inputStream.read(getBuffer());
                // Send the obtained bytes to the UI activity
            } catch (IOException e) {
                Log.i("BLUETOOTH",e.getMessage());
                break;
            }catch (InterruptedException e){
                Log.i("BLUETOOTH",e.getMessage());
                break;
            }
        }
    }
    public BluetoothDevice getDeviceByMAC(String mac){
        final Set<BluetoothDevice> pairedDevices = getPairedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // se tem o dispositivo pareado adiciona na lista
                if (device.getAddress().equals(mac)) {
                    this.device = device;
                    return device;
                }
            }
        }
        return null;
    }
    public Set<BluetoothDevice> getPairedDevices(){
        return mBluetoothAdapter.getBondedDevices();
    }
    public BluetoothDevice getPairedDevice(int index){
        return (BluetoothDevice) mBluetoothAdapter.getBondedDevices().toArray()[index];
    }
    public boolean connect(BluetoothDevice device){
        UUID uuid = UUID.randomUUID();
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            inputStream = socket.getInputStream();
        }catch (Exception e){
            Log.i("BLUETOOTH",e.getMessage());
            try {
                socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket",
                        new Class[]{int.class}).invoke(device, 1);
                socket.connect();
                inputStream = socket.getInputStream();
            }catch (Exception error){
                return false;
            }

        }

        this.start();
        return true;
    }
    public boolean connect(){
        return connect(device);
    }
    public boolean isConnected(){
        if(socket != null){
            return socket.isConnected();
        }
        return false;
    }

    public byte[] getBuffer() {
        return buffer;
    }
    public int getDistance(){
        if(this.isConnected()) {
            if (size == 1) {

                lastDisRead = buffer[0];
                if (lastDisRead < 0) {
                    lastDisRead = (128 - Math.abs(lastDisRead)) + 128;
                }
                return lastDisRead;
            }
            return lastDisRead;
        }
        return 0;
    }
    public void cancelComunication(){
        this.stop();
    }
}
