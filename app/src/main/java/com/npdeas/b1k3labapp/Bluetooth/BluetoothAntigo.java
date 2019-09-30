package com.npdeas.b1k3labapp.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.npdeas.b1k3labapp.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by NPDEAS on 09/04/2018.
 */

public class BluetoothAntigo {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int size = 0;
    private int lastDisRead = 0;
    private byte[] buffer = new byte[512];
    private String selectMac = "";
    private boolean keepRunning = false;

    public BluetoothAntigo() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // se nao tem bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {//caso o bluetooth nao esteja ativado, o ativa
            mBluetoothAdapter.enable();
            /*Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, Constants.REQUEST_WEB_DB_OK);*/
        }
    }
    public BluetoothAntigo(String mac){
        this();
        for(BluetoothDevice device: mBluetoothAdapter.getBondedDevices()){
            if(device.getAddress().equals(mac)){

            }
        }


    }


/*
    public Bluetooth(Context context, boolean connectByFile) {
        this();
        if (connectByFile) {
            File macFile = new File(context.getFilesDir() + "/B1K3_Lab", Constants.MAC_FILE);
            if (macFile.exists()) {
                try {
                    Scanner scanner = new Scanner(macFile);
                    selectMac = scanner.nextLine();
                    this.connect(this.getDeviceByMAC(selectMac));
                } catch (Exception e) {
                    Log.i("Bluetooth", e.getMessage());
                }
            }
        }
    }*/


    public Set<BluetoothDevice> getPairedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    public BluetoothDevice getPairedDevice(int index) {
        return (BluetoothDevice) mBluetoothAdapter.getBondedDevices().toArray()[index];
    }

    public void saveBluetoothDevice(Context context) {
        try {
            File root = new File(context.getFilesDir() + "/B1K3_Lab");
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, Constants.MAC_FILE);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(selectMac);
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }

    public boolean connect(BluetoothDevice device) {
        UUID uuid = UUID.randomUUID();
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            inputStream = socket.getInputStream();
        } catch (Exception e) {
            Log.i("BLUETOOTH", e.getMessage());
            try {
                socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket",
                        new Class[]{int.class}).invoke(device, 1);
                socket.connect();
                inputStream = socket.getInputStream();
            } catch (Exception error) {
                return false;
            }
        }
        this.selectMac = device.getAddress();
        return true;
    }

    public boolean isConnected() {
        if (socket != null) {
            return socket.isConnected();
        }
        return false;
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
