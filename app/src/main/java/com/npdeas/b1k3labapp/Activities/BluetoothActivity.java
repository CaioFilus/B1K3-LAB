package com.npdeas.b1k3labapp.Activities;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.npdeas.b1k3labapp.Bluetooth.Bluetooth;
import com.npdeas.b1k3labapp.Constants;
import com.npdeas.b1k3labapp.R;

import java.io.File;
import java.io.FileWriter;
import java.util.Set;

/**
 * Created by NPDEAS on 21/03/2018.
 */

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = BluetoothActivity.class.getSimpleName();

    private String mac;
    private Bluetooth bluetooth;
    private BluetoothDevice selectDevice;

    private ListView listView;
    private ArrayAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtViewResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        listView = findViewById(R.id.ListView);
        progressBar = findViewById(R.id.progressBar);
        txtViewResult = findViewById(R.id.txtViewResult);

        progressBar.setVisibility(View.INVISIBLE);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        bluetooth = new Bluetooth(this);

        //verifica se há algum  dispositivo pareado
        final Set<BluetoothDevice> pairedDevices = bluetooth.getPairedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // se tem o dispositivo pareado adiciona na lista
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

        /*
        * EVENTO DO CLICK NA LISTA
        * */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                progressBar.setVisibility(View.VISIBLE);// liga o progress bar
                //pega o dispositivo clicado
                selectDevice = bluetooth.getPairedDevice(position);
                mac = selectDevice.getAddress();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                try {
                    bluetooth.connect(selectDevice);// tenta conectar o BIKE LAB
                    runOnUiThread(new Runnable() {//thread para usar componetes da GUI
                        @Override
                        public void run() {
                            if (bluetooth.isConnected()) {// se sim aparece na tela
                                txtViewResult.setText("Conectado ao B1K3 LAB");
                                try {
                                    File root = new File(getFilesDir() +"/B1K3_Lab");
                                    if (!root.exists()) {
                                        root.mkdirs();
                                    }
                                    File file = new File(root , Constants.MAC_FILE);
                                    file.createNewFile();
                                    FileWriter writer = new FileWriter(file);
                                    writer.write(mac);
                                    writer.flush();
                                    writer.close();
                                }catch (Exception e){
                                    Log.i("FILE",e.getMessage());
                                }
                                finish();

                            } else {
                                // se não aparece q nao foi possivel
                                txtViewResult.setText("Não foi possivel conectar ao B1K3 LAB");
                            }
                            //deliga o progress bar
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {//thread para usar componetes da GUI
                        @Override
                        public void run() {
                            txtViewResult.setText("Timeout de conexão");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
                }).start();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
