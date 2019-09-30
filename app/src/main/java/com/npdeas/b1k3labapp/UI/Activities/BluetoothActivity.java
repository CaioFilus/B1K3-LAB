package com.npdeas.b1k3labapp.UI.Activities;

import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.npdeas.b1k3labapp.Bluetooth.IotDevice;
import com.npdeas.b1k3labapp.Database.AppDatabase;
import com.npdeas.b1k3labapp.Database.Daos.UserDao;
import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.R;

import java.util.List;

/**
 * Created by NPDEAS on 21/03/2018.
 */

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = BluetoothActivity.class.getSimpleName();

    private IotDevice bluetooth;
    private BluetoothDevice selectDevice;
    private List<BluetoothDevice> pairedDevices;


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
        bluetooth = IotDevice.getInstance(this);
        bluetooth.disconnect();

        //verifica se há algum  dispositivo pareado
        pairedDevices = bluetooth.getPairedDevices();
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
                new ConnectBluetooth(pairedDevices.get(position)).execute();

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


    class ConnectBluetooth extends AsyncTask<Void, Void, Boolean> {
        private BluetoothDevice device;

        public ConnectBluetooth(BluetoothDevice device) {
            this.device = device;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                bluetooth.connect(device.getAddress());// tenta conectar o BIKE LAB
                if (bluetooth.isConnected()) {// se sim aparece na tela
                    UserDao userDao = AppDatabase.getAppDatabase(BluetoothActivity.this).userDao();
                    User user = User.getCurrentUser();
                    user.bluetooth = device.getAddress();
                    userDao.update(user);
                    return true;
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.INVISIBLE);
            if (aBoolean) {
                BluetoothActivity.this.finish();
            } else {
                txtViewResult.setText("Não foi possivel conectar ao B1K3 LAB");
            }
        }
    }
}
