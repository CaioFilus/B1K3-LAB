package com.npdeas.b1k3labapp.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Pattern;

public class B1k3Lab extends IotDevice implements IotDevice.MensagemHandler,
        IotDevice.ConnHandler {

    private int distance = 0;
    private int temperature = 0;
    private int humidity = 0;
    private Context context;

    public B1k3Lab(Context context) {
        super(context);
        setMensageHandler(this);
//        setConnHandler(this);
    }

    public B1k3Lab(Context context, String mac) {
        super(context, mac);
        this.context = context;
        setConnHandler(this);
        setMensageHandler(this);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public void onRead(String message) {
        try {
            String datas[] = message.split(Pattern.quote("+"));
            if (datas.length >= 3) {
                float preview = Float.valueOf(datas[0]);
                if (preview != 0) {
                    distance = (int) preview;
                }
                temperature = (int) ((float) Float.valueOf(datas[1]));
                humidity = (int) ((float) Float.valueOf(datas[2]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadError(String message) {
        Log.i("BluetoothError", message);
    }

    @Override
    public void onConnected(BluetoothDevice device) {

    }

    @Override
    public void onConnError(BluetoothDevice device, String message) {
        distance = 0;
//        Toast.makeText(context, "Bluetooth desconectado", Toast.LENGTH_SHORT).show();
        new ConnectTask().execute();

    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    class ConnectTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            connect(mac);
            return null;
        }
    }
}
