package com.npdeas.b1k3labapp.Bluetooth;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.DeviceCallback;

public class IotDevice implements DeviceCallback {

    private static final String CONN_CONFIRMED = "+CONN:SUCCESS";
    private static IotDevice instance = null;

    private Bluetooth bluetooth;
    private Thread connectThread;
    protected String mac = "";
    private MensagemHandler mensagemHandler;
    private ConnHandler connHandler;
    private boolean isConnected = false;

    /*  Este construtor prepara o dispositivo para atuar como servidor.
     */
    public IotDevice(Context context) {
        instance = this;
        bluetooth = new Bluetooth(context);
        bluetooth.setDeviceCallback(this);
        bluetooth.onStart();
        bluetooth.enable();
//        bluetooth.
    }

    public IotDevice(Context context, String mac) {
        this(context);
        if (mac != null) {
            this.mac = mac;
            connect(mac);
        }
    }

    public static IotDevice getInstance(Context context) {
        if (instance == null) {
            instance = new IotDevice(context);
        }
        return instance;
    }

    public List<BluetoothDevice> getPairedDevices() {
        return bluetooth.getPairedDevices();
    }

    public boolean connect(String addr) {
        if (addr != null) {
            bluetooth.connectToAddress(addr);
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (Exception e) {
                Log.e("error on Wait", e.getMessage());
                return false;
            }
            return isConnected;
        } else {
            return false;
        }
    }

    public boolean connect() {
        if (!mac.equals("") && !bluetooth.isConnected()) {
            return connect(mac);
        }
        return true;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void stop() {
        bluetooth.onStop();
    }

    public void disconnect() {
        if (bluetooth.isConnected()) {
            bluetooth.disconnect();
        }
    }

    public void send(String msg) {
        bluetooth.send(msg);
    }

    public void setMensageHandler(MensagemHandler mensagemHandler) {
        this.mensagemHandler = mensagemHandler;
    }

    public void setConnHandler(ConnHandler connHandler) {
        this.connHandler = connHandler;

    }

    @Override
    public void onDeviceConnected(BluetoothDevice device) {
        isConnected = true;
        Log.i(this.getClass().getName(), device.getName());
        synchronized (this) {
            this.notify();
        }
        send(CONN_CONFIRMED);
        if (connHandler != null) {
            connHandler.onConnected(device);
        }
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {
        isConnected = false;
        Log.i(this.getClass().getName(), message);
        synchronized (this) {
            this.notify();
        }
        if (connHandler != null) {
            connHandler.onConnError(device, message);
        }
    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice device, String message) {
        isConnected = false;
        Log.i(this.getClass().getName(), message);
        synchronized (this) {
            this.notify();
        }
        if (connHandler != null) {
            connHandler.onConnError(device, message);
        }
    }

    @Override
    public void onMessage(String message) {
        if (mensagemHandler != null) {
            mensagemHandler.onRead(message);
        }
    }

    @Override
    public void onError(String message) {
        Log.e(this.getClass().getName(), message != null ? message : "sem mensagem");
        if (mensagemHandler != null) {
            mensagemHandler.onReadError(message);
        }
    }

    public interface MensagemHandler {
        void onRead(String message);

        void onReadError(String message);
    }

    public interface ConnHandler {

        void onConnected(BluetoothDevice device);

        void onConnError(BluetoothDevice device, String message);
    }

}